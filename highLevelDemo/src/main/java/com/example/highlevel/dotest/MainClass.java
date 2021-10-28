package com.example.highlevel.dotest;

public class MainClass {
    public static final long number = 10000001;

    int a = 0;
    volatile boolean flag = false;
    
    public static void main(String[] args) throws InterruptedException {
        
        MainClass mainClass = new MainClass();
//        concurrency();
//        serial();
        Thread thread1 = new Thread(() -> {
            mainClass.write();
        });
        Thread thread2 = new Thread(() -> {
            mainClass.read();
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(mainClass.a);
        System.out.println(mainClass.flag);
        
    }
    
    public void write() {
        a = 1;
        flag= true;
    }
    
    public void read(){
        if (flag) {
            int i = a;
        }
    }
    
    
    
    public static void concurrency() throws InterruptedException {
        Long start  = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            int a = 0;
            for (int i = 0; i < number; i++) {
                a += 5;
            }
        });
        thread.start();
        int b = 0;
        for (int j = 0;j < number; j++) {
            b--;
        }
        long end = System.currentTimeMillis();
        thread.join();
        System.out.println("并行时间："+(end-start)+";  b="+b);
    }
    
    public static void serial() {
        Long start  = System.currentTimeMillis();
        int a = 0;
        for (int i = 0; i < number; i++) {
            a += 5;
        }
        int b = 0;
        for (int j = 0;j < number; j++) {
            b--;
        }
        long end = System.currentTimeMillis();
        System.out.println("串行时间："+(end-start)+";  b="+b);
    }
    
}
