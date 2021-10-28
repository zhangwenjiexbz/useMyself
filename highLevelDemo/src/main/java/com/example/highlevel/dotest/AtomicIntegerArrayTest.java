package com.example.highlevel.dotest;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author Sebastian
 */
public class AtomicIntegerArrayTest {
    
    static int[] value = {1,2};
    static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        ai.getAndSet(0,3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);
    }
    
}
