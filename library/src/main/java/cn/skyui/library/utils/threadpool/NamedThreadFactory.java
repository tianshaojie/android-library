package cn.skyui.library.utils.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 指定Thread线程名，方便在日志或工具内定位
 * @author tianshaojie
 * @date 2018/1/5
 */
public class NamedThreadFactory implements ThreadFactory {

    private AtomicInteger count = new AtomicInteger(0);

    private String threadName;

    public NamedThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(threadName + count.addAndGet(1));
        return thread;
    }
}
