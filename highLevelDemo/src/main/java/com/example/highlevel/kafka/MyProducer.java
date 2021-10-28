package com.example.highlevel.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/**
 * @author Sebastian
 */
public class MyProducer {
    
    private static KafkaProducer<String,String> producer;
    
    //初始化
    static {
        Properties properties = new Properties();
        //kafka启动，生产者建立连接broker的地址
        properties.put("bootstrap.servers","127.0.0.1:9092");
        //kafka序列化方式
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //自定义分区分配器
        properties.put("partitioner.class","com.example.highlevel.kafka.CustomPartitioner");
        producer = new KafkaProducer<>(properties);
    }

    /**
     * 创建topic：.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181
     * --replication-factor 1 --partitions 1 --topic kafka-study
     * 创建消费者：.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092
     * --topic imooc-kafka-study --from-beginning
     * 
     * 
     * 发送消息，发送完后不做处理
     */
    private static void sendMessageForgetResult() {
        ProducerRecord<String,String> record = new ProducerRecord<>("kafka-study","name","ForgetResult");
        producer.send(record);
        producer.close();
    }

    /**
     * 发送同步消息，获取发送的消息
     */
    private static void sendMessageSync () throws Exception {
        ProducerRecord<String,String> recorder = new ProducerRecord<>("kafka-study","name","sync");
        RecordMetadata metadata = producer.send(recorder).get();
        // imooc-kafka-study
        System.out.println(metadata.topic());
        // 分区为0
        System.out.println(metadata.partition());
        // 发送了一条消息，此时偏移量+1
        System.out.println(metadata.offset());
        producer.close();
    }

    
    /**
     * 创建topic：.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181
     * --replication-factor 1 --partitions 3 --topic kafka-study-x
     * 创建消费者：.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092
     * --topic kafka-study-x --from-beginning
     */
    private static void sendMessageCallBack() {
        ProducerRecord<String,String> recorder = new ProducerRecord<>("kafka-study-x","name","done");
        producer.send(recorder,new MyProducerCallBack());
        recorder = new ProducerRecord<>("kafka-study-x","name-x","done");
        producer.send(recorder,new MyProducerCallBack());
        producer.close();
    }

    /**
     * 发送异步消息
     * 场景：每条消息发送有延迟，多条消息发送，无需同步等待，可以执行其他操作，程序会自动异步调用
     */
    private static class MyProducerCallBack implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }
            System.out.println("*** MyProducerCallBack ***");
            System.out.println(metadata.topic());
            System.out.println(metadata.partition());
            System.out.println(metadata.offset());
        }
    }

    public static void main(String[] args) {
        sendMessageCallBack();
    }
    
}
