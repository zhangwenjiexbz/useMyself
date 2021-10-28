package com.example.highlevel.dotest;

import java.util.concurrent.CountDownLatch;

/**
 * @author Sebastian
 */
public class CountDownLatchTest {
    
    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 finish");
            c.countDown();
        });
        
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 finish");
            c.countDown();
        });
        
        thread1.start();
        thread2.start();
        
        c.await();

        System.out.println("all finish");
        
    }
    
}
