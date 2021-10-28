package com.example.highlevel.dotest;

import sun.awt.Mutex;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class HeldExclusivelyLock implements Lock {
   
    private static class Sync extends AbstractQueuedSynchronizer {
        
        // 是否处于占用状态
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1; 
        }

        // 当前状态为时获取锁
        @Override
        public boolean tryAcquire(int acquires) {
            if (compareAndSetState(0,1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 释放锁,将状态设置为0
        @Override
        protected boolean tryRelease(int releases) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }
    
    private final Sync sync = new Sync();
    
    
    
    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    /**
     * 超时时间内没获取到同步状态返回false
     */
    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
    
    public boolean isLocked() {
        return sync.isHeldExclusively();
    }
    
    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }
}
