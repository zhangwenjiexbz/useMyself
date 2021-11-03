package com.example.highlevel.controller;

import com.example.highlevel.pojo.TestUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Sebastian
 */
@RestController
@RequestMapping("/redis")
@SuppressWarnings("all")
public class TestRedisController {
    
    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedisController.class);
    
    @RequestMapping("/test")
    public void testFirst() {
        redisTemplate.opsForValue().set("name","aaa");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
        LOGGER.info(String.valueOf(name));

        Map<String,Object> map = new HashMap<>();
        for (int i = 0;i < 10;i++) {
            TestUser user = new TestUser("user"+i,i+10);
            map.put("user"+i,user);
        }
        redisTemplate.opsForHash().putAll("test",map);
        BoundHashOperations test = redisTemplate.boundHashOps("test");
        Map entries = test.entries();
        System.out.println(entries);
    }
    
    @RequestMapping("/stringAndHash")
    public Map<Object,Object> testStringAndHash() {
        redisTemplate.opsForValue().set("k1","v1",1000, TimeUnit.MILLISECONDS);
        //注意这里使用了 JDK 的序列化器 ,所以 Redis 保存时不是整数, 不能运算
        redisTemplate.opsForValue().set("int_key","1");
        stringRedisTemplate.opsForValue().set("int","1");

        //使用运算
        stringRedisTemplate.opsForValue().increment("int",2);

        Map<String, Object> hash = new HashMap<>();
        hash.put("field1", "value1");
        hash.put("field2", "value2");
        stringRedisTemplate.opsForHash().putAll("hash2",hash);
        stringRedisTemplate.opsForHash().put("hash2","field3","value3");
        // 单独取一个值
        Object o = stringRedisTemplate.opsForHash().get("hash2", "field3");
        System.out.println(o);

        //绑定散列操作的 key,这样可以连续对同一个散列数据类型进行操作
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps("hash2");
        hashOps.delete("field2");
        hashOps.put("field4","value4");
        LOGGER.info(hashOps.entries().toString());

        Map<Object, Object> entries = hashOps.entries();
        return entries;
    }
    
    @RequestMapping("/list")
    public List<String> testList() {
        // 链表从左到右的顺序为v10, v8, v6, v4, v2
        stringRedisTemplate.opsForList().leftPushAll("list1","v2","v4","v6","v8","v10");
        // 链表从左到右的顺序为v1, v3, v5, v7, v9
        stringRedisTemplate.opsForList().rightPushAll("list2","v1","v3","v5","v7","v9");

        // 绑定list2操作链表
        BoundListOperations<String, String> listOps = stringRedisTemplate.boundListOps("list2");
        String s = listOps.rightPop();
        LOGGER.info("list2最右边的元素是:{}",s);

        String index = listOps.index(1);
        LOGGER.info("下标为1的元素是:{}",index);
        
        listOps.leftPush("v0");

        Long size = listOps.size();
        LOGGER.info("链表长为:{}",size);

        List<String> range = listOps.range(0, listOps.size() - 1);
        LOGGER.info("现在的链表为:{}",range.toString());
        
        return range;
    }
    
    @RequestMapping("/set")
    public Set<String> testSet() {
        stringRedisTemplate.opsForSet().add("set1","v1","v1","v3","v5","v7","v9");
        stringRedisTemplate.opsForSet().add("set2","v2","v4","v6","v7","v10","v10");

        BoundSetOperations<String, String> setOps = stringRedisTemplate.boundSetOps("set1");
        setOps.add("v11","v13");
        setOps.remove("v1","v3");

        Set<String> members = setOps.members();
        LOGGER.info("现在集合中所有元素:{}",members.toString());

        Long size = setOps.size();
        LOGGER.info("现在集合的长度:{}",size);

        Set<String> interSet = setOps.intersect("set2");
        setOps.intersectAndStore("set2","set1_2");
        LOGGER.info("新的交集为:{}",interSet.toString());

        Set<String> diff = setOps.diff("set2");
        setOps.diffAndStore("set2","set1&2");
        LOGGER.info("新的差集为:{}",diff.toString());

        Set<String> union = setOps.union("set2");
        setOps.unionAndStore("set2","set1+2");
        LOGGER.info("新的并集为:{}",union.toString());
        
        return union;
    }
    
    @RequestMapping("/zset")
    public Set<String> testZSet() {
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            double score = i * 0.1;
            ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>("value"+i,score);
            set.add(typedTuple);
        }
        LOGGER.info("新建zset:{}",set);
        
        stringRedisTemplate.opsForZSet().add("zset1",set);
        BoundZSetOperations<String, String> zSetOps = stringRedisTemplate.boundZSetOps("zset1");
        
        zSetOps.add("value11",0.26);
        Set<String> range = zSetOps.range(1, 6);
        LOGGER.info("下标为1-6的集合为:{}",range.toString());

        // 按分数排序获取有序集合
        Set<String> rangeByScore = zSetOps.rangeByScore(0.2, 0.6);
        LOGGER.info("权重为0.2-0.6之间的顺序集合为:{}",rangeByScore.toString());
        
        // 定义范围
        RedisZSetCommands.Range range1 = new RedisZSetCommands.Range();
        range1.gt("value3");
        range1.lte("value8");

        Set<String> set1 = zSetOps.rangeByLex(range1);
        LOGGER.info("按值排序:{}",set1.toString());
        
        zSetOps.remove("value9","value2");
        LOGGER.info("删除value9和value2");

        Double value8 = zSetOps.score("value8");
        LOGGER.info("value8的分数为:{}",value8);

        // 在下标区间 按分数排序, 同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> set2 = zSetOps.rangeWithScores(1, 6);
        LOGGER.info("在下标区间 按分数排序, 同时返回value和score:{}",set2.toString());

        // 在下标区间 按分数排序, 同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> scoreSet = zSetOps.rangeByScoreWithScores(1,6);
        LOGGER.info("在下标区间 按分数排序, 同时返回value和score:  "+scoreSet.toString());

        Set<String> set3 = zSetOps.reverseRange(2, 8);
        LOGGER.info("从大到小排序:{}",set3);
        
        return set3;

    } 
    
    
}
