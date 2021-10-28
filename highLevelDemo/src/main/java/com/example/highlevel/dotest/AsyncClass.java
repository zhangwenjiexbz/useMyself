package com.example.highlevel.dotest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class AsyncClass {
    
    @Async("getTaskExecutor")
    public void doTaskExtra(CountDownLatch countDownLatch,int i) throws InterruptedException {
        System.out.println("开始执行额外任务"+i+".....");
        System.out.println("当前线程："+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        Thread.sleep(1000);
        long l1 = System.currentTimeMillis();
        System.out.println("额外任务"+i+"执行完成，共耗时: "+(l1-l));
        countDownLatch.countDown();
    }
    
}
