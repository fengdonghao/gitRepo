package com.fengdonghao.server.aspect;

import com.fengdonghao.server.bean.Boy;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ Author:fdh
 * @ Description: 消息队列监听器
 * @ Date： Create in 14:19 2017/12/22
 */
@Component
public class Receiver {

    @RabbitListener(queues = "queue")
    public void receiver(String str){
        System.out.println(str);
    }

    @RabbitListener(queues = "queue")
    public void receiverBoy(Boy boy){

        System.out.println(boy.toString());
    }

  /*  @RabbitListener(queues = "topic.message")
    public String process1(byte[]  str){
        System.out.println("message ："+str);
        Boy boy=new Boy();
        Map map = (HashMap)SerializationUtils.deserialize(str);
        System.out.println(map);
        return "success";
    }*/

    /*@RabbitListener(queues = "topic.messages")
    public String  process2(String str1) {
        System.out.println(Thread.currentThread().getName()+"接收到来自topic.message队列的消息: "+str1+" 并进行反序列化");

        File file = new File("E:\\WebPackage\\a.txt");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            Boy newUser = (Boy) ois.readObject();
            System.out.println("反序列之后"+newUser);
            System.out.println("反序列之后getname:"+newUser.getName());
            System.out.println("反序列之后getAge"+newUser.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       System.out.println("messages ："+str1);
        System.out.println(Thread.currentThread().getName()+"接收到来自topic.message队列的消息: "+str1);
        return " ---success---";
    }*/
  /*  @RabbitListener(queues = "topic.message")
    public String  receiver(Thread currentThread){

        System.out.println("-----"+currentThread.getState()+"-----");
        return "success";
    }*/



}
