package com.example.highlevel.dotest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Sebastian
 */
public class SemaphoreTest {
    
    private static ExecutorService threadPool = Executors.newFixedThreadPool(30);
    
    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            threadPool.execute(() -> {
                try {
                    s.acquire();
                    System.out.println(Thread.currentThread().getName() + " save data");
                    s.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            if (s.availablePermits() == 0) {
                Thread.sleep(1000);
                System.out.println();
            }
        }
        threadPool.shutdown();
    }
    
}
