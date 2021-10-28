package com.example.highlevel.dotest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastian
 */
public class DoSomeTest implements Runnable{

    Map<String,Integer> map = new HashMap<>();
    
    void doTest() {
        System.out.println("111");
        map.put("a",1);
    }

    @Override
    public void run() {
        System.out.println("123");
        map.put("b",2);
    }
    
    public static void main(String[] args) {
        DoSomeTest testRun = new DoSomeTest();
        testRun.doTest();
    }
    
}

//class TestRun implements Runnable {
//
//    void doTest() {
//        System.out.println("111");
//    }
//    
//    @Override
//    public void run() {
//        System.out.println("123");
//    }
//}
