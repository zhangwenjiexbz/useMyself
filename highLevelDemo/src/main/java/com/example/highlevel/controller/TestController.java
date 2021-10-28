package com.example.highlevel.controller;

import com.alibaba.ttl.TtlRunnable;
import com.example.highlevel.dotest.AsyncClass;
import com.example.highlevel.pojo.FoodDetail;
import com.example.highlevel.pojo.TestPojo;
import com.example.highlevel.pojo.Type;
import com.example.highlevel.request.BaseRequestBody;
import com.example.highlevel.service.FoodRepository;
import com.example.highlevel.service.FoodTypeRepository;
import com.example.highlevel.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;
    
    @Autowired
    private TestService testService;
    
    @Autowired
    private AsyncClass asyncClass;
    
    @Resource
    private FoodTypeRepository foodTypeRepository;
    
    @Resource
    private FoodRepository foodRepository;
    
    @RequestMapping("/doSomeTest")
    public TestPojo doSomeTest(){
        try {
            System.out.println("执行主线任务");
            Thread.sleep(200);
            CountDownLatch countDownLatch = new CountDownLatch(5);
            for (int i = 0; i < 5; i++) {
                asyncClass.doTaskExtra(countDownLatch,i);
            }
            countDownLatch.await();
            for (int i = 0; i < 10; i++) {
                System.out.println("继续执行主线任务"+i);
                Thread.sleep(10);
            }
            
            System.out.println("所有任务都执行完成");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new TestPojo();
    }

    @RequestMapping("/testMoreThread")
    public TestPojo testMoreThread() {
        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            System.out.println("放入任务一");
            futures.add(CompletableFuture.runAsync(TtlRunnable.get(() -> {
                try {
                    testService.doServiceFirst();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }),taskExecutor));
            System.out.println("放入任务二");
            futures.add(CompletableFuture.runAsync(TtlRunnable.get(() -> {
                try {
                    testService.doServiceSecond();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }),taskExecutor));
            System.out.println("放入任务三");
            futures.add(CompletableFuture.runAsync(TtlRunnable.get(() -> {
                try {
                    testService.doServiceThird();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }),taskExecutor));

            System.out.println("处理主线程......："+Thread.currentThread().getName());
            CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
            all.join();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new TestPojo();
    }
    
    @RequestMapping("/allType")
    public List<Type> findAllType() {
        return foodTypeRepository.readAllByIdIsNotNull();
    }

    @RequestMapping("/type")
    public List<Type> findType(@RequestBody BaseRequestBody requestBody) {
        
        return foodTypeRepository.queryTypeById(requestBody.getId());
    }

    @RequestMapping("/type/{id}")
    public List<Type> findTypeById(@PathVariable Integer id) {

        return foodTypeRepository.queryTypeById(id);
    }

    @RequestMapping("/food/detail")
    public List<FoodDetail> findFoodDetail() {

        List<Object[]> foodJoinTypeAndSupplier = foodRepository.getFoodJoinTypeAndSupplier();

        List<FoodDetail> foodDetails = null;
        try {
            foodDetails = castEntity(foodJoinTypeAndSupplier, FoodDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodDetails;

    }
    
    public <T> List<T> castEntity(List<Object[]> objects,Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        List<T> returnList = new LinkedList<>();

        Object[] objectArr = objects.get(0);

        Class[] clazzList = new Class[objectArr.length];

        for (int i = 0; i < objectArr.length; i++) {
            if (objectArr[i] != null) {
                clazzList[i] = objectArr[i].getClass();
            } else {
                clazzList[i] = String.class;
            }
        }
        
        for (Object[] o : objects) {
            Constructor<T> constructor = clazz.getConstructor(clazzList);
            returnList.add(constructor.newInstance(o));
        }
        
        return returnList;
        
    }
            
}
