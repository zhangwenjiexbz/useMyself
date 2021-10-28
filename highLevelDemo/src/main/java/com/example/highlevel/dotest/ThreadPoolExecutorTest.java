package com.example.highlevel.dotest;

import com.example.highlevel.config.ThreadPoolExecutorSingleton;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Sebastian
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = ThreadPoolExecutorSingleton.getThreadPoolExecutor();

        try {
            threadPoolExecutor.execute(() -> System.out.println("finish"));

            Future<Integer> submit = threadPoolExecutor.submit(() -> 123);
            Integer x = submit.get();
            System.out.println(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            threadPoolExecutor.shutdown();
        }
    }
    
}
