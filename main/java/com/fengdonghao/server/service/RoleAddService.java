package com.fengdonghao.server.service;

import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.SysRoleRepository;
import com.fengdonghao.server.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:fdh
 * @Description:
 * @Date：Create in9:38 2017/12/16
 */
@Service
public class RoleAddService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private SysRoleRepository sysRoleRepository;
    public void insertTwo(){
        UserInfo userInfoA=new UserInfo();
        userInfoA.setUsername("test");
        userInfoA.setPassword("test");
        userInfoA.setName("test");
        userInfoA.setSalt("22222");

        userInfoRepository.save(userInfoA);

        UserInfo userInfoB=new UserInfo();
        userInfoB.setUsername("test1");
        userInfoB.setPassword("test1");
        userInfoB.setName("test1");

        userInfoRepository.save(userInfoB);

        /*SysRole sysRoleA=new SysRole();
        sysRoleA.setRole("comUser");
        sysRoleA.setDescription("普通用户");
        sysRoleA.setAvailable(true);
        sysRoleRepository.save(sysRoleA);*/


    }



}
