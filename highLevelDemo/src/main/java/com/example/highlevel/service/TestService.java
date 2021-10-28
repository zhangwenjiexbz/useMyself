package com.example.highlevel.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    
    public void doServiceFirst() throws InterruptedException {
        System.out.println("开始任务1.....");
        System.out.println("当前线程："+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        Thread.sleep(1111);
        long l1 = System.currentTimeMillis();
        System.out.println("任务1执行完成，共耗时: "+(l1-l));
    }

    public void doServiceSecond() throws InterruptedException {
        System.out.println("开始任务2.....");
        System.out.println("当前线程："+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        Thread.sleep(2222);
        long l1 = System.currentTimeMillis();
        System.out.println("任务2执行完成，共耗时: "+(l1-l));
    }

    public void doServiceThird() throws InterruptedException {
        System.out.println("开始任务3.....");
        System.out.println("当前线程："+Thread.currentThread().getName());
        long l = System.currentTimeMillis();
        Thread.sleep(3333);
        long l1 = System.currentTimeMillis();
        System.out.println("任务3执行完成，共耗时: "+(l1-l));
    }
    
}
