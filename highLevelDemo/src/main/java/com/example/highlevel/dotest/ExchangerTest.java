package com.example.highlevel.dotest;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sebastian
 */
public class ExchangerTest {
    
    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);
    
    private static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        
        threadPool.execute(() -> {
            String A = "银行流水A";
            try {
                exchanger.exchange(A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.execute(() -> { 
            String B = "银行流水B";
            try {
                String A = exchanger.exchange("B");
                System.out.println(A.equals(B));
                System.out.println("A:"+A);
                System.out.println("B:"+B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        threadPool.shutdown();
        
    }
    
}
