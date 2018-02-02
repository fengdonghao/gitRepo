package com.fengdonghao.server.service;

import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:45 2018/1/11
 */

@ServerEndpoint("/ws/{username}")
public class WSServer {
    private String currentUser;
    @OnOpen
    public void onOpen(@PathVariable("username") String username, Session session) throws IOException{
        currentUser=username;
        System.out.println("connected..."+session.getId());

    }
    @OnMessage
    public String onMessage(String message,Session session){
        System.out.println(currentUser+":"+message);
        return currentUser+":"+message;
    }
    @OnClose
    public void onClose(Session session, CloseReason closeReason){
        System.out.println(String.format("Session %s closed because of %s",session.getId(),closeReason));

    }

    @OnError
    public void onError(Throwable throwable){
        throwable.printStackTrace();
    }

}
