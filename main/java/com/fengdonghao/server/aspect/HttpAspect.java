package com.fengdonghao.server.aspect;


import com.fengdonghao.server.bean.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
@Component
public class HttpAspect {
    private final static Logger logger= LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.fengdonghao.server.controller.UserInfoController.*(..)) || execution(public * com.fengdonghao.server.controller.HomeController.*(..))||execution(public * com.fengdonghao.server.controller.HomeController.logOut())||execution(public * com.fengdonghao.server.controller.AppUserInfoController.*(..))")
    public void log(){
    }

    @Pointcut("execution(public * com.fengdonghao.server.controller.HomeController.index())")
    public void log1(){

    }
    @After("log1()")
    public void doAfter1(){
        System.out.println("----用户登录成功---");
        Subject currentUser= SecurityUtils.getSubject();
        Session session=currentUser.getSession();
        UserInfo userInfo=(UserInfo)currentUser.getPrincipal();
        System.out.println(userInfo);
        System.out.println(session.getStartTimestamp());
        System.out.println(session.getId());
    }


    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        logger.info("start");
        //url
        logger.info("url={}",request.getRequestURL());

        //method
        logger.info("method={}",request.getMethod());

        //ip
        logger.info("ip={}",request.getRemoteAddr());

        //类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringType()+"."+joinPoint.getSignature().getName());

        //参数
        logger.info("arg={}",joinPoint.getArgs());



    }
    @After("log()")
    public void doAfter(JoinPoint joinPoint){
        logger.info("end");

        /* System.out.println(req.getMethod());
        //获取项目名称
        System.out.println(req.getContextPath());
        //获取完整请求路径
        System.out.println(req.getRequestURL());

        //获取请求参数
        System.out.println(req.getQueryString());*/
        /*System.out.println("----------------------------------------------------------");
        //获取请求头
        String header = req.getHeader("user-Agent");
        System.out.println(header);
        header = header.toLowerCase();
        //根据请求头数据判断浏览器类型
        if(header.contains("chrome")){
            System.out.println("您的访问浏览器为谷歌浏览器");
        }else if(header.contains("msie")){
            System.out.println("您的访问浏览器为IE浏览器");
        }else if(header.contains("firefox")){
            System.out.println("您的访问浏览器为火狐浏览器");
        }else{
            System.out.println("您的访问浏览器为其它浏览器");
        }
        System.out.println("----------------------------------------------------------");
        //获取所有的消息头名称
        Enumeration<String> headerNames = req.getHeaderNames();
        //获取获取的消息头名称，获取对应的值，并输出
        while(headerNames.hasMoreElements()){
            String nextElement = headerNames.nextElement();
            System.out.println(nextElement+":"+req.getHeader(nextElement));
        }
        System.out.println("----------------------------------------------------------");
        //根据名称获取此重名的所有数据
        Enumeration<String> headers = req.getHeaders("accept");
        while (headers.hasMoreElements()) {
            String string = (String) headers.nextElement();
            System.out.println(string);
        }*/

    }
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturning(Object object) throws NullPointerException{

        if (object==null){
            throw new NullPointerException();
        }else{
            logger.info("response={}", object.toString());

        }



    }
}
