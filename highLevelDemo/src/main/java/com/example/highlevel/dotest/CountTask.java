package com.example.highlevel.dotest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author Sebastian
 */
public class CountTask extends RecursiveTask<Integer> {
    
    private static final int THRESHOLD = 2;
    
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        
        int sum = 0;
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end)/2;
            CountTask countTaskLeft = new CountTask(start,middle);
            CountTask countTaskRight = new CountTask(middle + 1,end);
            countTaskLeft.fork();
            countTaskRight.fork();

            Integer left = countTaskLeft.join();
            Integer right = countTaskRight.join();
            
            sum = left + right;
        }
        
        return sum;
    }

    public static void main(String[] args) {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(1,10);
        ForkJoinTask<Integer> submit = forkJoinPool.submit(countTask);
        try {
            System.out.println(submit.get());
            if (!countTask.isCompletedAbnormally()) {
                System.out.println(countTask.getException());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

