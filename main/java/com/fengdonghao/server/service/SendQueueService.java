package com.fengdonghao.server.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:10 2017/12/27
 */
@Service
public class SendQueueService implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            System.out.println("消息发送成功1:"+correlationData);
        }else{
            System.out.println("消息发送失败:"+cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println(message.getMessageProperties().getCorrelationIdString()+"发送失败");
    }

    public void send(Thread  msg){
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("开始发送消息 : " + msg);
        Object response=rabbitTemplate.convertSendAndReceive("exchange","topic.message",msg,correlationId);
        System.out.println("结束发送消息 : " + msg);
        System.out.println("消费者响应 : " + response + " 消息处理完成");



    }
}

