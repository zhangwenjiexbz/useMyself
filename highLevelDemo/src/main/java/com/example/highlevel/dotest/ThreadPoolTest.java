package com.example.highlevel.dotest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    
    public static MyThreadPool myThreadPool = new MyThreadPool(5);

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(5);
        
        myThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("111");
            System.out.println("还剩下："+countDownLatch.getCount());
            countDownLatch.countDown();
            Thread.currentThread().interrupt();
        });
        myThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("222");
            System.out.println("还剩下："+countDownLatch.getCount());
            countDownLatch.countDown();
            Thread.currentThread().interrupt();
        });
        myThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("333");
            System.out.println("还剩下："+countDownLatch.getCount());
            countDownLatch.countDown();
            Thread.currentThread().interrupt();
        });
        myThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("444");
            System.out.println("还剩下："+countDownLatch.getCount());
            countDownLatch.countDown();
            Thread.currentThread().interrupt();
        });
        myThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("555");
            System.out.println("还剩下："+countDownLatch.getCount());
            countDownLatch.countDown();
            Thread.currentThread().interrupt();
        });

        countDownLatch.await();

        System.out.println("所有线程执行完成");
    }
}
