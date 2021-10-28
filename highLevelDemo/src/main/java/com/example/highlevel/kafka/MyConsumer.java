package com.example.highlevel.kafka;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Properties;

/**
 * @author Sebastian
 */
public class MyConsumer {
    
    private static KafkaConsumer<String,String> consumer;
    
    private static Properties properties;
    
    static {
        properties = new Properties();
        // 建立连接broker的地址
        properties.put("bootstrap.servers","127.0.0.1:9092");
        // kafka反序列化
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        // 指定消费者组
        properties.put("group.id", "KafkaStudy");
    }

    /**
     * 自动提交位移：由consume自动管理提交
     */
    @SuppressWarnings("all")
    private static void generalConsumeMessageAutoCommit() {
        // 配置
        properties.put("enable.auto.commit",true);
        consumer = new KafkaConsumer<>(properties);
        
        // 指定topic
        consumer.subscribe(Collections.singleton("kafka-study-x"));
        
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> r : records) {
                    System.out.printf(
                            "topic = %s, partition = %s, key = %s, value = %s%n",
                            r.topic(), r.partition(), r.key(), r.value()
                    );
                    //消息发送完成
                    if ("done".equals(r.value())) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动同步提交当前位移，根据需求提交，但容易发送阻塞，提交失败会进行重试直到抛出异常
     */
    @SuppressWarnings("all")
    private static void generalConsumeMessageSyncCommit() {
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("kafka-study-x"));
        while (true) {
            boolean flag = true;
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> r : records) {
                System.out.printf(
                        "topic = %s, partition = %s, key = %s, value = %s%n",
                        r.topic(), r.partition(), r.key(), r.value()
                );
                if ("done".equals(r.value())) { flag = false; }
            }
            try {
                //手动同步提交
                consumer.commitSync();
            } catch (CommitFailedException ex) {
                System.out.println("commit failed error: " + ex.getMessage());
            }
            if (!flag) { 
                break; 
            }
        }

    }

    /**
     * 手动异步提交当前位移，提交速度快，但失败不会记录
     */
    @SuppressWarnings("all")
    private static void generalConsumeMessageAsyncCommit() {
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("kafka-study-x"));
        while (true) {
            boolean flag = true;
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> r : records) {
                System.out.printf(
                        "topic = %s, partition = %s, key = %s, value = %s%n",
                        r.topic(), r.partition(), r.key(), r.value()
                );
                if ("done".equals(r.value())) { flag = false; }
            }
             consumer.commitAsync();
            if (!flag) {
                break;
            }
        }

    }

    /**
     * 手动异步提交当前位移，提交速度快，但失败不会记录
     */
    @SuppressWarnings("all")
    private static void generalConsumeMessageAsyncCommitWithCallBack() {
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("kafka-study-x"));
        while (true) {
            boolean flag = true;
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> r : records) {
                System.out.printf(
                        "topic = %s, partition = %s, key = %s, value = %s%n",
                        r.topic(), r.partition(), r.key(), r.value()
                );
                if ("done".equals(r.value())) { flag = false; }
            }
            consumer.commitAsync((map, e) -> {
                if (e != null) {
                    System.out.println("commit failed for offsets: " + e.getMessage());
                }
            });
            if (!flag) {
                break;
            }
        }

    }

    /**
     * 混合同步与异步提交位移
     */
    @SuppressWarnings("all")
    private static void mixSyncAndAsyncCommit() {
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("kafka-study-x"));
        try {
            while (true) {
                //boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, " + "value = %s",
                            record.topic(), record.partition(),
                            record.key(), record.value()
                    ));
                    //if (record.value().equals("done")) { flag = false; }
                }
                //手动异步提交，保证性能
                consumer.commitAsync();
                //if (!flag) { break; }
            }
        } catch (Exception ex) {
            System.out.println("commit async error: " + ex.getMessage());
        } finally {
            try {
                //异步提交失败，再尝试手动同步提交
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    @SuppressWarnings("all")
    public static void main(String[] args) throws FileNotFoundException {
//        chooseAndSaveFile();
        // 自动
//        generalConsumeMessageAutoCommit();
        // 手动同步
//        generalConsumeMessageSyncCommit();
        // 手动异步
//        generalConsumeMessageAsyncCommit();
        // 手动异步带回调
//        generalConsumeMessageAsyncCommitWithCallBack();
        // 混合
        mixSyncAndAsyncCommit();
    }

    @SuppressWarnings("all")
    private static void chooseAndSaveFile() throws FileNotFoundException {
        int result = 0;
        File file = null;
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        //这里重要的一句
        FileSystemView fsv = FileSystemView.getFileSystemView();
        //得到桌面路径
        System.out.println(fsv.getHomeDirectory());
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("请选择要上传的文件...");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
            System.out.println("path: "+path);
            File f = new File(path);
            String name = f.getName();
            FileInputStream in = new FileInputStream(f);
            OutputStream os = null;
            try {
                String p = "D:\\testFile\\";
                // 2、保存到临时文件
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流保存到本地文件

                File tempFile = new File(p);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                os = new FileOutputStream(tempFile.getPath() + File.separator + name);
                // 开始读取
                while ((len = in.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 完毕，关闭所有链接
                try {
                    os.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

