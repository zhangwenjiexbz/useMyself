package com.example.highlevel.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Sebastian
 * 我们是在生产者中声明|创建的exchange、queue，如果生产者尚未运行，
 * 并且rabbitmq上没有对应的exchange、queue（之前没有创建），
 * 启动消费者，消费者要监听指定的queue，根本就连接不上queue，
 * 更加健壮的写法是：在消费者中也声明exchange、queue，有就直接用，没有会自动创建。
 */
public class Consumer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    /**
     * 队列名
     */
    private static final String QUEUE_NAME = "my_queue";
    /**
     * 交换机名
     */
    private static final String EXCHANGE_NAME = "my_exchange";
    /**
     * 交换机类型
     */
    private static final String EXCHANGE_TYPE = "topic";
    /**
     * 路由
     */
    private static final String EXCHANGE_ROUTING_KEY = "my_routing_key.#";
    
    @SuppressWarnings("all")
    public static void receive() throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        // 通过信道声明一个exchange，若已存在则直接使用，不存在会自动创建
        // 参数：name、type、是否支持持久化、此交换机没有绑定一个queue时是否自动删除、是否只在rabbitmq内部使用此交换机、此交换机的其它参数（map）
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, false, null);

        // 通过信道声明一个queue，如果此队列已存在，则直接使用；如果不存在，会自动创建
        // 参数：name、是否支持持久化、是否是排它的、是否支持自动删除、其他参数（map）
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 将queue绑定至某个exchange。一个exchange可以绑定多个queue
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, EXCHANGE_ROUTING_KEY);
        
        // 创建消费者，指定要使用的channel。QueueingConsume类已经弃用，使用DefaultConsumer代替
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body);
                    // 设置了一直监听，系统会卡住
                    // int i = 5/0;
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    LOGGER.info("接收到消息:{}", message);
                } catch (Exception e) {
                    channel.basicReject(envelope.getDeliveryTag(),true);
                    LOGGER.warn("拒绝接收消息");
                }
            }
        };
        
        // 监听指定的queue。会一直监听。
        // 参数：要监听的queue、是否自动确认消息、使用的Consumer
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }

}
