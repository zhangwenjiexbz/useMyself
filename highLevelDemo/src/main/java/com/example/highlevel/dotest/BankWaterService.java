package com.example.highlevel.dotest;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Sebastian
 */
public class BankWaterService implements Runnable{
    
    private CyclicBarrier c = new CyclicBarrier(4,this);
    
    private ConcurrentHashMap<String,Integer> concurrentHashMap = new ConcurrentHashMap<>();
    
    private Executor executor = Executors.newFixedThreadPool(4);
    
    private void count() {
        for (int i = 0; i < 4; i++) {
            executor.execute(() -> {
                concurrentHashMap.put(Thread.currentThread().getName(),1);
                try {
                    c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }  
    @Override
    public void run() {
        
        int result = 0;
        for (Map.Entry<String,Integer> entry : concurrentHashMap.entrySet()) {
            result += entry.getValue(); 
        }
        
        concurrentHashMap.put("result",result);
        System.out.println(concurrentHashMap);
        System.out.println(concurrentHashMap.get("result"));
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
