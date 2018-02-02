package com.fengdonghao.server.bean;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 15:15 2018/1/10
 */
public class RespMsg {

   private  String respMsg;

    public RespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
