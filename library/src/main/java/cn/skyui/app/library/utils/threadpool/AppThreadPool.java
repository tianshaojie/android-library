package cn.skyui.app.library.utils.threadpool;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * APP全局线程池：建议执行小而分散的任务
 * 调用方法：AppThreadPool.getInstance.execute(new PriorityBlockingQueue())
 * 1. 可以指定PriorityBlockingQueue的优先级：new PriorityBlockingQueue(PriorityConstants.PRIORITY_NORMAL)
 * 2. 如果任务执行时间较长不可控：建议使用submit方法，获取Future返回值，可做取消、超时等操作
 * @author tianshaojie
 * @date 2018/1/5
 */
public class AppThreadPool {

    private static volatile AppThreadPool instance;

    private ThreadPoolExecutor threadPoolExecutor;
    private PriorityBlockingQueue queue;

    private AppThreadPool() {
        init();
    }

    public static AppThreadPool getInstance() {
        if(instance == null) {
            synchronized (AppThreadPool.class) {
                if(instance == null) {
                    instance = new AppThreadPool();
                }
            }
        }
        return instance;
    }

    /**
     * 此方法仅在Application.onTerminate调用，不做多线程同步处理
     */
    public static void releaseAndShutdown() {
        if(instance != null) {
            instance.shutdown();
            instance = null;
        }
    }

    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小---2
     * maximumPoolSize 最大线程池大小---5
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间---60+单位TimeUnit
     * TimeUnit keepAliveTime时间单位---TimeUnit.SECONDS
     * workQueue
     *      new PriorityBlockingQueue<>(50); 带优先级阻塞队列---50容量的阻塞队列(注：PriorityBlockingQueue为无界队列)
     *      new LinkedBlockingQueue<>(); 链表结构组成的无界阻塞队列 (注：LinkedBlockingQueue性能远高于PriorityBlockingQueue)
     * threadFactory 新建线程工厂---new NamedThreadFactory()---定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maximumPoolSize+workQueue之和时，即当提交第56个任务时会交给RejectedExecutionHandler来处理
     */
    private void init() {
        queue = new PriorityBlockingQueue<>(10, new PriorityCompare<>());
        threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                60,
                TimeUnit.SECONDS,
                queue,
                new NamedThreadFactory("AppThreadPool"),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    /**
     * 在未来某个时间执行给定的命令
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param command 命令
     */
    public void execute(final PriorityRunnable command) {
        if(!threadPoolExecutor.isShutdown()) {
            threadPoolExecutor.execute(command);
        }
    }

    /**
     * 在未来某个时间执行给定的命令链表
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param commands 命令链表
     */
    public void execute(final List<PriorityRunnable> commands) {
        for (PriorityRunnable command : commands) {
            if(!threadPoolExecutor.isShutdown()) {
                threadPoolExecutor.execute(command);
            }
        }
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @param <T>    泛型
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(final PriorityRunnable task, final T result) {
        if(threadPoolExecutor.isShutdown()) {
            return null;
        }
        return threadPoolExecutor.submit(task, result);
    }

    /**
     * 手动清楚队列Task，在切换直播间时很有必要
     */
    public void clear() {
        if(queue != null) {
            queue.clear();
        }
    }

    /**
     * One good way to shut down the ExecutorService (which is also recommended by Oracle)
     * http://www.baeldung.com/java-executor-service-tutorial
     */
    private void shutdown() {
        try {
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow();
        }
    }

}
