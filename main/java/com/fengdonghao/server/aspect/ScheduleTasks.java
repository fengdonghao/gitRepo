package com.fengdonghao.server.aspect;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:fdh
 * @Description: 创建一个定时任务
 * @Date：Create in15:36 2017/12/11
 */
@Component
public class ScheduleTasks {

    private Thread currentThread;
    private static final SimpleDateFormat format=new SimpleDateFormat("HH(hh):mm:ss S");
    private static final Logger logger= LoggerFactory.getLogger(ScheduleTasks.class);

    @Scheduled(fixedRate = 120000,initialDelay = 10000)
    public void firstScheduleTask(){



        logger.info("定时任务执行，现在的时间是："+format.format(new Date()));

        logger.info("当前线程："+ Thread.currentThread().getName());
    }





}
