package com.fengdonghao.server.repository;

import com.fengdonghao.server.bean.UserInfo;

public interface UserInfoService {
    //通过username查询用户信息
    public UserInfo findByUsername(String username);






}
