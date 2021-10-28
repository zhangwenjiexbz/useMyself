package com.example.highlevel.dotest;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.TimeUnit;

public class Piped {

    public static void main(String[] args) throws IOException, InterruptedException {
//        PipedWriter out = new PipedWriter();
//        PipedReader in = new PipedReader();
//        try {
//            out.connect(in);
//            Thread printThread = new Thread(new Print(in), "printThread");
//            printThread.start();
//            int receive = 0;
//            while ((receive = System.in.read()) != -1) {
//                out.write(receive);
//            }
//        } finally {
//            out.close();
//            in.close();
//        }
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new DoJoin(previous),String.valueOf(i));
            thread.start();
            previous = thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println("当前线程："+Thread.currentThread().getName()+" 执行完成");
    }
    
    static class DoJoin implements Runnable {

        private Thread prevoious;

        public DoJoin(Thread prevoious) {
            this.prevoious = prevoious;
        }

        @Override
        public void run() {
            try {
                prevoious.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前线程："+Thread.currentThread().getName()+" 执行完成");
        }
    }
    
    static class Print implements Runnable {
        private PipedReader in;

        public Print(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            try {
                while((receive = in.read()) != -1) {
                    System.out.print((char) receive);
                }
            } catch (IOException e) {
                
            }
        }
    }
}

