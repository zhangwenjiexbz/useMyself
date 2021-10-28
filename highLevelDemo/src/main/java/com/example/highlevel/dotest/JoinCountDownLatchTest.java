package com.example.highlevel.dotest;

/**
 * @author Sebastian
 */
public class JoinCountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        
        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 finish"); 
        });
        
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 finish");
        });
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();

        System.out.println("all finish");
    }
    
}
