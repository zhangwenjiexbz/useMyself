package com.example.highlevel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Sebastian
 */
@ComponentScan({ "com.example.highlevel.config",
        "com.example.highlevel.controller",
        "com.example.highlevel.dotest",
        "com.example.highlevel.gitconfig",
        "com.example.highlevel.service" })

@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients
public class HighlevelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HighlevelApplication.class, args);
    }

}
