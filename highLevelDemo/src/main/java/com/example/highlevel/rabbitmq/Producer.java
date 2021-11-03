package com.example.highlevel.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Sebastian
 */
public class Producer {

    /**
     * 队列名称
     */
    private static final String QUEUE_NAME = "my_queue";
    /**
     * 要使用的exchange名称
     */
    private static final String EXCHANGE_NAME = "my_exchange";
    /**
     * 要使用的exchange类型
     */
    private static final String EXCHANGE_TYPE = "topic";
    /**
     * exchange使用的routing_key
     */
    private static final String EXCHANGE_ROUTING_KEY = "my_routing_key.#";
    
    public static void send() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        
        // 通过工厂创建连接
        Connection connection = connectionFactory.newConnection();
        // 通过连接创建信道
        Channel channel = connection.createChannel();
        // 通过信道声明一个exchange，若已存在则直接使用，不存在会自动创建
        // 参数：name、type、是否支持持久化、此交换机没有绑定一个queue时是否自动删除、是否只在rabbitmq内部使用此交换机、此交换机的其它参数（map）
        channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE,true,false,false,null);

        // 通过信道声明一个queue，如果此队列已存在，则直接使用；如果不存在，会自动创建
        // 参数：name、是否支持持久化、是否是排它的、是否支持自动删除、其他参数（map）
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);

        // 将queue绑定至某个exchange。一个exchange可以绑定多个queue
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,EXCHANGE_ROUTING_KEY);

        //发送消息
        String message = "hello";
        String routingKey = "my_routing_key.key1";

        // 消息是byte[]，可以传递所有类型（转换为byte[]），不局限于字符串
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes());
        System.out.println("send message:"+message);
        
        // 关闭连接
        channel.close();
        connection.close();
    }
    
}
