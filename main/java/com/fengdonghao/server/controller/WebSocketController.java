package com.fengdonghao.server.controller;

import com.fengdonghao.server.bean.ReqMsg;
import com.fengdonghao.server.bean.RespMsg;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 15:03 2018/1/10
 */
@Controller
public class WebSocketController {


    @MessageMapping("/hello")
    @SendTo("/msg/getResp")
    public RespMsg show(ReqMsg reqMsg){
        System.out.println(reqMsg.getReqmsg());
        RespMsg respMsg=new RespMsg("你好"+reqMsg.getReqmsg());
        return respMsg;
    }
    @RequestMapping("/webSocket")
    public String  socket(){
        return "wc";
    }
    @RequestMapping("/wsWebSocket")
    public String  socket1(){
        return "wsWebSocket";
    }
}
