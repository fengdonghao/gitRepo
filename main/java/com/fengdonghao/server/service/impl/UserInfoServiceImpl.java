package com.fengdonghao.server.service.impl;

import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.repository.UserInfoService;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;
    private static final Logger logger= LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Override
    public UserInfo findByUsername(String username) {
        logger.info("开始执行：UserInfoServiceImpl.findByUsername()");
        if(userInfoRepository.findByUsername(username)!=null){
            return userInfoRepository.findByUsername(username);
        }else {
            System.out.println("当前用户不存在！");
            throw new UnknownAccountException() ;


        }

    }




}
