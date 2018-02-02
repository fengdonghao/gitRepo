package com.fengdonghao.server.transition;

import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.repository.UserInfoService;
import org.jolokia.detector.ServerHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:25 2018/1/26
 */

@Component
public class Tools {

    @Autowired
    private UserInfoRepository userInfoService;

    private static Tools serverHandler;
    @PostConstruct
    public void init(){
        serverHandler = this;
        serverHandler.userInfoService = this.userInfoService;
    }
    public  String tool(String username,String oder){
        System.out.println("username:"+username);
        System.out.println("oder:"+oder);
        System.out.println(Thread.currentThread().getId()+" : "+Thread.currentThread().getName());

        if (serverHandler.userInfoService.findByUsername(username)!=null){
            UserInfo userInfo=serverHandler.userInfoService.findByUsername(username);
            System.out.println("用户的信息是："+userInfo);
            return "success";
        }else{
            return "error";
        }
//        System.out.println(userInfo);
//        UserInfo userInfo = userInfoService.findByUsername(username);



    }

}
