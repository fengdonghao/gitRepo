package com.fengdonghao.server.repository;

import com.fengdonghao.server.bean.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    //通过username查找用户信息
    public UserInfo findByUsername(String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update userInfo set state =?1 where uid=?2",nativeQuery = true)
    int updateStatusById(int state, Long uid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update userInfo set headPath =?1 where username=?2",nativeQuery = true)
    int updateHeadByUsername(String headPath, String username);


}
