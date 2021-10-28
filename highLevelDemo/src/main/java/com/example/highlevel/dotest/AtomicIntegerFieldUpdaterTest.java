package com.example.highlevel.dotest;

import com.example.highlevel.pojo.TestUser;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author Sebastian
 */
public class AtomicIntegerFieldUpdaterTest {
    
    public static AtomicIntegerFieldUpdater<TestUser> updater = AtomicIntegerFieldUpdater.newUpdater(TestUser.class,"age");

    public static void main(String[] args) {
        TestUser aa = new TestUser("aa",10);
        System.out.println(updater.getAndIncrement(aa));
        System.out.println(updater.get(aa));
    }
    
}
