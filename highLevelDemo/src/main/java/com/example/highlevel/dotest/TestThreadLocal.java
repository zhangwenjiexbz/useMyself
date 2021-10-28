package com.example.highlevel.dotest;

import java.util.concurrent.TimeUnit;

public class TestThreadLocal {
    public static final ThreadLocal<Long> THREAD_LOCAL_TIME = ThreadLocal.withInitial(System::currentTimeMillis);
    
    public static final void begin () {
        THREAD_LOCAL_TIME.set(System.currentTimeMillis());
    }
    
    public static final Long end () {
        return  System.currentTimeMillis() - THREAD_LOCAL_TIME.get();
    }

    public static void main(String[] args) throws InterruptedException {
        begin();
        
        TimeUnit.SECONDS.sleep(2);
        System.out.println("cast:"+end());
        THREAD_LOCAL_TIME.remove();
    }
}
