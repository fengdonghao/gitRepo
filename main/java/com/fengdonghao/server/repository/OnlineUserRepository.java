package com.fengdonghao.server.repository;

import com.fengdonghao.server.bean.OnlineUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 14:53 2018/1/22
 */

public interface OnlineUserRepository extends JpaRepository<OnlineUser,Long>{

    @Transactional
    public OnlineUser deleteBySessionid(String sessionId);

    @Transactional
    public OnlineUser findBySessionid(String sessionId);

    @Transactional
    public OnlineUser findByUsername(String sessionId);
}
