package com.example.highlevel.dotest;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ZWJ
 */
public class SimpleHttpServer {

    public static void main(String[] args) {
        String s = "D:\\highLevelDemo\\src\\main\\resources\\webapp\\Test.html/favicon.ico";
        String replace = s.replace("\\Test.html", "");
        System.out.println(replace);
    }
     
    static MyThreadPool<HttpRequestHandler> threadPool = new MyThreadPool<>();

    /**
     * SimpleHttpServer 的根路径
     */
    static String basePath;
    
    static ServerSocket serverSocket;

    /**
     * 监听端口
     */
    static int port = 8080;

    public static void setBasePath(String basePath) {
        System.out.println(new File(basePath).exists());
        System.out.println(new File(basePath).isDirectory());
        if (!StringUtils.isEmpty(basePath) && new File(basePath).exists()) {
            SimpleHttpServer.basePath = basePath;
        }
    }

    public static void setPort(int port) {
        if (port > 0) {
            SimpleHttpServer.port = port;
        }
    }

    /**
     * 启动 SimpleHttpServer
     * @throws Exception
     */
    public static void start () throws Exception{
        serverSocket = new ServerSocket(port);
        System.out.println(">>>>>>>>>>连接到端口:"+port);
        Socket socket = null;
        while((socket = serverSocket.accept()) != null) {
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    static class HttpRequestHandler implements Runnable{
        
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String line = null;
            BufferedReader br = null;
            BufferedReader reader = null;
            PrintWriter out = null;
            InputStream in = null;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String header = reader.readLine();
                System.out.println("herder:"+header);
                System.out.println(header.split(" ")[1]);
                System.out.println("基础地址:"+basePath);
                // 由相对路径计算出绝对路径
                String filePath = basePath;
                if (header.split(" ")[1].length()>1) {
                    filePath = basePath.replace("\\Test.html","") + header.split(" ")[1];
                }
                System.out.println("绝对路径:" + filePath);
                out = new PrintWriter(socket.getOutputStream());
                if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    in = new FileInputStream(filePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int i = 0;
                    while ((i = in.read()) != -1) {
                        baos.write(i);
                    }
                    byte[] byteArray = baos.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("SERVER MOLLY");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: "+byteArray.length);
                    out.println("");
                    socket.getOutputStream().write(byteArray,0,byteArray.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("HTTP/1.1 200 OK");
                    out.println("SERVER MOLLY");
                    out.println("Content-Type: text/html; Charset: UTF-8");
                    out.println("");
                    while((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            } finally {
                close(br,in,reader,out,socket);
            }
        }
    }
    
    private static void close (Closeable...closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
