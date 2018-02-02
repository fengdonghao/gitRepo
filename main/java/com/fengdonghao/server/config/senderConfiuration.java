package com.fengdonghao.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:fdh
 * @Description:
 * @Date： Create in 16:00 2017/12/22
 */
@Configuration
public class senderConfiuration {
    @Bean(name = "message")
    public Queue queueMessage(){
        return new Queue("topic.message");
    }
    @Bean(name = "messages")
    public Queue queueMessages(){
        return new Queue("topic.message");
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("exchange");

    }
    @Bean
    Binding bindingExchangeMessage(@Qualifier("message") Queue queueMessage, TopicExchange exchange){
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }
    @Bean
    Binding bindingExchangeMessages(@Qualifier("messages") Queue queueMessages,TopicExchange exchange){
        return BindingBuilder.bind(queueMessages).to(exchange).with("top.#");
    }
}
