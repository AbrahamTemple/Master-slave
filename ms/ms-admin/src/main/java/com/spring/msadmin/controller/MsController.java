package com.spring.msadmin.controller;

import com.spring.msadmin.service.RabbitMqService;
import com.spring.msadmin.service.impl.MsOrderServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
public class MsController {

    @Resource
    MsOrderServiceImpl msOrderService;

    @Resource
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Resource
    private RabbitMqService rabbitMQService;

    @RequestMapping("/ms/{user}/{spend}")
    public String addOrder(@PathVariable(value = "user") String user,@PathVariable(value = "spend") Integer spend){
        String pid = UUID.nameUUIDFromBytes((user+new Date().getTime()).getBytes()).toString();
        return rabbitMQService.MakeMsOrder(user,pid,spend);
    }


//    @RequestMapping(value = "/order/{name}")
//    public String order(@PathVariable(value = "name") String name){
//        return msOrderService.getPidByName(name);
//    }
//
//    @RequestMapping(value = "/testRedis")
//    public String testRedis() {
//        msOrder o = new msOrder("哆啦A梦","cae58fb3-465b-408f-9e0b-c9bed030bdb2",64);
//        redisTemplate.opsForValue().set("user1",o,120, TimeUnit.SECONDS);
//        return "redis test success!";
//    }
//
//    @RequestMapping(value = "/testMQ")
//    public String testRabbitMQ() {
//        return rabbitMQService.MakeOrder();
//    }
}
