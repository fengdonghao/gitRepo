

package com.fengdonghao.server.exception;



/**
 * @Author:fdh
 * @Description: 自定义异常类
 * @Date：Create in16:34 2017/12/12
 */


public class BusinessException extends RuntimeException {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public BusinessException(String code,String msg){
        super();
        this.code=code;
        this.msg=msg;
    }
}


