package com.example.highlevel.dotest;

import com.example.highlevel.pojo.TestPojo;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Sebastian
 */
public class AtomicReferenceTest {
    
    public static AtomicReference<TestPojo> atomicReference = new AtomicReference<>();

    public static void main(String[] args) {
        TestPojo testPojo = new TestPojo("aaa","ha",15);
        atomicReference.set(testPojo);
        TestPojo update = new TestPojo("bbb","b",17);
        atomicReference.compareAndSet(testPojo,update);
        System.out.println(atomicReference.get().getName());
        System.out.println(atomicReference.get().getAge());
    }
}
