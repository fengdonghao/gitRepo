

package com.fengdonghao.shiro.exception;

import com.fengdonghao.server.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.ManyToMany;
import java.util.HashMap;
import java.util.Map;




/**
 * @Author:fdh
 * @Description: 定义全局统一异常处理
 * @Date：Create in16:23 2017/12/12
 */



@ControllerAdvice
public class MyControllerException {



/**
    *@Description: 全局捕获异常，只要作用在@RequestMapping方法上，所有的异常信息都会被捕获到
    *@Data:16:25 2017/12/12
    */



    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String,Object> errorHander(Exception e){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",-1);
        map.put("msg",e.getMessage());
        return map;

    }
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Map<String,Object> errorHander(BusinessException e){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",e.getCode());
        map.put("msg",e.getMsg());
        return map;

    }
}



