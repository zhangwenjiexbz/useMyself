package com.example.highlevel.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Sebastian
 */
@RestController
@RequestMapping("/redis")
public class TestRedisController {
    
    @Resource
    private RedisTemplate redisTemplate;
    
    @RequestMapping("/test")
    public void testFirst() {
        redisTemplate.opsForValue().set("name","aaa");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
    
}
