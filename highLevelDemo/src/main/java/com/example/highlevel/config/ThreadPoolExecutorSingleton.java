package com.example.highlevel.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Sebastian
 */
public final class ThreadPoolExecutorSingleton {
    
    private static ThreadPoolExecutor threadPoolExecutor = null;
    
    private ThreadPoolExecutorSingleton() {}
    
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            synchronized (ThreadPoolExecutorSingleton.class) {
                if (threadPoolExecutor == null) {
                    ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue(10);
                    threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1500, TimeUnit.MILLISECONDS, arrayBlockingQueue,new ThreadPoolExecutor.CallerRunsPolicy());
                    
                }
            }
        }

        return threadPoolExecutor;
    }
    
}
