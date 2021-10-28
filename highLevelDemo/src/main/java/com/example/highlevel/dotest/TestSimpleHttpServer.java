package com.example.highlevel.dotest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.highlevel.pojo.TestPojo;

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author ZWJ
 */
public class TestSimpleHttpServer {
    
    public static void main(String[] args) throws Exception {
        
//        SimpleHttpServer.setBasePath("D:\\highLevelDemo\\src\\main\\resources\\webapp\\Test.html");
//        
//        SimpleHttpServer.start();

        TestPojo testPojo = new TestPojo();
        testPojo.setMessage("");    
        testPojo.setName("");

        String s = testPojo.toString();
        System.out.println(s);

        int i = s.indexOf("{");
        String substring = s.substring(i, s.length());
        String s2 = substring.replace("=", ":");
        System.out.println(s2);

        String s1 = JSON.toJSONString(testPojo);
        System.out.println(s1);

        TestPojo t = JSON.parseObject(s2,TestPojo.class);
        System.out.println(t);

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue(1000,true);
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>(1000, (o1, o2) -> o1-o2);
        priorityBlockingQueue.add(1);
    }
    
}
