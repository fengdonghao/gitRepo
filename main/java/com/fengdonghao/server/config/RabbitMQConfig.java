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
 * @Date： Create in 11:08 2017/12/27
 */
@Configuration
public class RabbitMQConfig {
    /**
     *@Description： 新建队列 topic.message
     *@Data:16:13 2017/12/22
     */
    @Bean(name = "message")
    public Queue queueMessage(){
        return new Queue("topic.message");
    }

    /**
     *@Description: 交换机
     *@Data:16:15 2017/12/22
     */
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange("exchange");

    }
    /**
     *@Description: 交换机与消息队列进行绑定 queue：message绑定 topic.message 和 队列messages绑定交换机with topic.#
     *@Data:16:18 2017/12/22
     */
    @Bean
    Binding bindingExchangeMessage(@Qualifier("message") Queue queueMessage, TopicExchange exchange){
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

}

