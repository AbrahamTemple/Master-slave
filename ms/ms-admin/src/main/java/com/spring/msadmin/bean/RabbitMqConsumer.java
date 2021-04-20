package com.spring.msadmin.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.msadmin.entity.msOrder;
import com.spring.msadmin.service.impl.MsOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@RabbitListener(queues = "ms_order")
public class RabbitMqConsumer {

    @Resource
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;

    @Resource
    MsOrderServiceImpl msOrderService;

    @RabbitHandler
    public void MsOrderMessage(String rid) {
        log.info(redisTemplate.opsForValue().get(rid).toString());
        msOrder order = (msOrder) redisTemplate.opsForValue().get(rid);
        msOrderService.putUserOrder(order.getName(),order.getPid(),order.getSpend());
    }

//    @RabbitHandler
//    public void receiveMessage(String msg){
//        System.out.println("测试消息："+msg);
//    }
}
