package com.example.highlevel.dotest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ZWJ
 */
public class TestReadAndWrite {
    
    public static Map<String,Object> map = new HashMap<>();
    
    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock r = readWriteLock.readLock();
    static Lock w = readWriteLock.writeLock();
    
    public static final Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }
    
    public static final Object set(String key, Object value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }
    
    public static final void cleanAll() {
        w.lock();
        try {
           map.clear(); 
        } finally {
            w.unlock();
        }
    }
}
