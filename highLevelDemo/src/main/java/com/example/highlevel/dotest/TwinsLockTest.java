package com.example.highlevel.dotest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author ZWJ
 */
public class TwinsLockTest {

    public static void main(String[] args) throws InterruptedException {
        final Lock twinsLock = new TwinsLock();
        
        class Worker extends Thread {

            @Override
            public void run() {
                while(true) {
                    twinsLock.lock();
                    try{
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName());
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        twinsLock.unlock();
                    }
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker();
            worker.setDaemon(true);
            worker.start();
        }
        
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println();
        }
    }
}
