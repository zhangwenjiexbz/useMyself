package com.example.highlevel.controller;

import com.example.highlevel.rabbitmq.Consumer;
import com.example.highlevel.rabbitmq.Producer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Sebastian
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/mq")
public class TestRabbitMQController {
    
    @RequestMapping("/test")
    public void testRabbit() {
        try {
            Producer.send();
            Consumer.receive();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    
}
