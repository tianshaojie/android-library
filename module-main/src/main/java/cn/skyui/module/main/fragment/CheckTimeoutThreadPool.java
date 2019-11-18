package cn.skyui.module.main.fragment;

import android.support.annotation.NonNull;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tianshaojie
 *
 * 专用于 webview 加载超时检查的线程池，避免每次创建线程池
 */
public class CheckTimeoutThreadPool {

    private static SynchronousQueue<Runnable> queue;
    private static ThreadPoolExecutor threadPoolExecutor;

    private static void init() {
        queue = new SynchronousQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                queue,
                new NamedThreadFactory("WebViewTimeoutThreadPool"),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    /**
     * 在未来某个时间执行给定的命令
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param command 命令
     */
    public static void execute(final Runnable command) {
        if(threadPoolExecutor == null) {
            init();
        }
        if(!threadPoolExecutor.isShutdown()) {
            threadPoolExecutor.execute(command);
        }
    }

    /**
     * 清除队列
     */
    public static void clear() {
        if(queue != null) {
            queue.clear();
        }
    }

    /**
     * One good way to shut down the ExecutorService (which is also recommended by Oracle)
     * http://www.baeldung.com/java-executor-service-tutorial
     */
    public static void shutdown() {
        if(threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
            return;
        }
        try {
            threadPoolExecutor.shutdown();
            if (!threadPoolExecutor.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                threadPoolExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPoolExecutor.shutdownNow();
        }
    }

    public static class NamedThreadFactory implements ThreadFactory {

        private AtomicInteger count = new AtomicInteger(0);

        private String threadName;

        public NamedThreadFactory(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(threadName + count.addAndGet(1));
            return thread;
        }
    }
}
