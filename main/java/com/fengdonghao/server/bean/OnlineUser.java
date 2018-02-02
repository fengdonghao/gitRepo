package com.fengdonghao.server.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:05 2018/1/18
 */
@Entity
public class OnlineUser {

    @Id
    @GeneratedValue
    Long id;

    String sessionid;

    String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionid;
    }

    public void setSessionId(String sessionId) {
        this.sessionid = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
