package com.fengdonghao.server.controller;

import com.sun.deploy.security.UserDeclinedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class exceptionController {

/**
*@Description: 捕获UnauthorizedException抛出的异常
 *             当用户没有权限时，抛出异常，捕获到后返回403.HTML
*@Data: 15:20 2017/12/7
*/
    @ExceptionHandler(value = UnauthorizedException.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e)  {
        return "403";
    }
    @ResponseBody
    @ExceptionHandler(value = SizeLimitExceededException.class)
    public String defaultErrorHandler1(HttpServletRequest req, Exception e)  {
        return "文件大小超过限制";
    }

    @ExceptionHandler(value = UserDeclinedException.class)
    public String defaultErrorHandler2(HttpServletRequest req, Exception e)  {
        return "403";
    }

}
