package com.example.highlevel.dotest;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author  ZWJ
 */
public class FairAndUnfairTest {
    
    private static Lock fairLock = new ReentrantLock2(true);
    private static Lock nonFairLock = new ReentrantLock2(false);
    
    @Test
    public void fair() {
        testLock(fairLock);
    } 
    
    @Test
    public void unfair() {
        testLock(nonFairLock);
    }
    
    private void testLock(Lock lock) {
        for (int i = 0; i < 5; i++) {
            Job job = new Job(lock);
            job.start();
        }
    }
    
    private static class Job extends Thread {
        
        private Lock lock;
        
        public Job(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println("当前线程:" + Thread.currentThread().getName() + "----等待队列中线程:" + ((ReentrantLock2) lock).getQueuedThreads().stream().map(Thread::getName).collect(Collectors.toList()));
            } finally {
                lock.unlock();
            }
            lock.lock();
            try {
                System.out.println("当前线程:" + Thread.currentThread().getName() + "----等待队列中线程:" + ((ReentrantLock2) lock).getQueuedThreads().stream().map(Thread::getName).collect(Collectors.toList()));
            } finally {
                lock.unlock();
            }
        }
    }
    
    private static class ReentrantLock2 extends ReentrantLock {
        
        public ReentrantLock2(boolean fair) {
            super(fair);
        }

        @Override
        public Collection<Thread> getQueuedThreads() {
            List<Thread> list = new ArrayList<>(super.getQueuedThreads());
            Collections.reverse(list);
            return list;
        }
    }
}
