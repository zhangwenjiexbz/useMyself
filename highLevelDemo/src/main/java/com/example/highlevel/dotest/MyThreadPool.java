package com.example.highlevel.dotest;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ZWJ
 */
@Component("threadPool")
public class MyThreadPool<Job extends Runnable>{
    
    /**
     * 线程池最大限制数
     */
    private static final int MAX_WORKER_NUMBERS = 10;
    
    /**
     * 线程池默认的数量
     */
    private static final int DEFAULT_WORKER_NUMBERS = 2;
    
    /**
     * 线程池最小的数量
     */
    private static final int MIN_WORKER_NUMBERS = 1;
    
    /**
     * 这是个工作列表，会向里面插入工作
     */
    private final LinkedList<Job> jobs = new LinkedList<>();
    
    /**
     * 工作者列表
     */
    private final LinkedList<Worker> workers = new LinkedList<>();
    
    /**
     * 工作者线程的数量
     */
    private int workNum = DEFAULT_WORKER_NUMBERS;
    
    /**
     * 线程编号生成
     */
    private AtomicLong threadNum = new AtomicLong(); 

    public MyThreadPool() {
        initialWorkers(DEFAULT_WORKER_NUMBERS);
    }

    public MyThreadPool(int workNum) {
        this.workNum = workNum> MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : workNum < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : workNum;
        initialWorkers(workNum);
    }
    
    public void execute(Job job) {
        if (job != null) {
            synchronized (jobs) {
                jobs.add(job);
                jobs.notify();
            }
        }
    }
    
    public void shutDown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }
    
    public void addWorkers(int num) {
        synchronized (jobs) {
            if (num + this.workNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workNum;
            }
            initialWorkers(num);
            this.workNum += num;
        }
    }
    
    public void removeWork(int num) {
        synchronized (jobs) {
            if (num > workNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            // 按照给定的数量停止worker
            int count = 0;
            while(count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workNum -= count;
        }
    }
    
    public int getJobSize() {
        return jobs.size();
    }

    private void initialWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker,"ThreadPool-Worker-"+threadNum.incrementAndGet());
            thread.start();
        }
    }
    
    class Worker implements Runnable{

        private volatile boolean running = true;
        
        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("打断等待状态");
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        
                    }
                }
            }
        }
        
        public void shutdown() {
            running = false;
        }
    }
}
