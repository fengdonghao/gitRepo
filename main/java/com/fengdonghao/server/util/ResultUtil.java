package com.fengdonghao.server.util;

import com.fengdonghao.server.bean.Result;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 10:57 2018/1/24
 */
public class ResultUtil {

    public static Result success(Object object){
        Result result=new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(object);
        return result;
    }
    public static Result success1(){
        Result result=new Result();
        result.setCode(0);
        result.setMsg("success");
        result.setData(null);
        return result;
    }

    public static Result error(Integer code,String msg){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


}
