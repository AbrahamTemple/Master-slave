package com.spring.msadmin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.msadmin.entity.msOrder;
import com.spring.msadmin.service.impl.MsOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RabbitMqService {

    private final String exchangeName = "ms_exchange";
    private final String routerKey = "test";

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    public String MakeMsOrder(String name, String pid, int spend){
        msOrder order = new msOrder(name, pid, spend);
        String rid = UUID.nameUUIDFromBytes(pid.getBytes()).toString();
        redisTemplate.opsForValue().set(rid,order,10, TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend(exchangeName,routerKey,rid);
        return "order is already committed, and it's pending!";
    }

//    public String MakeOrder(){
//        String orderId = UUID.randomUUID().toString();
//        /* 消息队列分发订单
//         * @param 交换机
//         * @param 路由key/队列名
//         * @param 消息内容
//         */
//        String exchangeName = "ms_exchange";
//        String routerKey = "test";
//
//        rabbitTemplate.convertAndSend(exchangeName,routerKey,orderId);
//        return orderId;
//    }
}
