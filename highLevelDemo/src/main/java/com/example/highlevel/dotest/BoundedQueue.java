package com.example.highlevel.dotest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sebastian
 */
public class BoundedQueue<T> {
    
    private Object[] items;
    /**
     * 添加的下标、删除的下标、数组当前的数量
     */
    private int addIndex, removeIndex, count;
    private Lock lock = new ReentrantLock();
    Condition nonEmpty = lock.newCondition();
    Condition nonFull = lock.newCondition();

    public BoundedQueue(int size) {
        items = new Object[size];
    }
    
    public void add(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                nonFull.await();
            }
            items[addIndex] = t;
            if (++addIndex == items.length) {
                addIndex = 0;
            }
            ++count;
            nonEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                nonEmpty.await();
            }
            Object x = items[removeIndex];
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            nonFull.signal();
            return (T)x;
        } finally {
            lock.unlock();
        }
    }
}
