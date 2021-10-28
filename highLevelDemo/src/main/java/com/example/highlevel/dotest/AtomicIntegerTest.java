package com.example.highlevel.dotest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sebastian
 */
public class AtomicIntegerTest {
    
    public static AtomicInteger ai = new AtomicInteger(1);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement());
        System.out.println(ai.get());
    }
    
}
