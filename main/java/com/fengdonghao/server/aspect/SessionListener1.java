package com.fengdonghao.server.aspect;

import com.fengdonghao.server.bean.OnlineUser;
import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.OnlineUserRepository;
import com.sun.deploy.security.UserDeclinedException;
import org.omg.CORBA.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:00 2018/1/10
 */
@WebListener
public class SessionListener1 implements HttpSessionListener, HttpSessionAttributeListener{


    @Autowired
    private OnlineUserRepository onlineUserRepository;

    public static final Logger logger= LoggerFactory.getLogger(SessionListener1.class);
/*    public void util(HttpServletRequest request){
        HttpSession httpSession=request.getSession();
        String[] value=httpSession.getValueNames();
        System.out.println(value);
        Subject currentUser=SecurityUtils.getSubject();
        Session session=currentUser.getSession();
        currentUser.getSession().getAttributeKeys();

        System.out.println(session.getAttribute("HOST_SESSION_KEY"));
        System.out.println(currentUser.getSession().getAttributeKeys());
//        String username=userInfo.getUsername();
        System.out.println("username");
    }*/


    @Override
    public void  attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        logger.info("--attributeAdded--");
        HttpSession session=httpSessionBindingEvent.getSession();
        logger.info("key----:"+httpSessionBindingEvent.getName());
        logger.info("value---:"+httpSessionBindingEvent.getValue());
      /*  if (httpSessionBindingEvent.getName().equals("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY")){
            System.out.println("--------");
            *//*List<UserInfo> list= List<UserInfo> httpSessionBindingEvent.getValue();
            System.out.println(userInfo);*//*
            *//* User user=(User)httpSessionBindingEvent.getValue();
            System.out.println(user.getUsername());
            HttpSession session=httpSessionBindingEvent.getSession();
            session.setAttribute("username","fdh123");
            Object name=httpSessionBindingEvent.getSession().getAttribute("username");*//*
        }*/
        /**
        *@ Description:当session里面的值是用户名时，把该session和username保存到数据库中onlineUser
         * 就是在线的人数
         *
        *@ Data:9:56 2018/1/23
        */
        if (httpSessionBindingEvent.getName().equals("username")){
            OnlineUser onlineUser=new OnlineUser();
            String username=(String)httpSessionBindingEvent.getValue();
            if (onlineUserRepository.findByUsername(username)!=null){
                logger.info("该用户已登录！"+username);
                session.invalidate();
            }else{
                onlineUser.setUsername(username);
                onlineUser.setSessionId(session.getId());
                onlineUserRepository.save(onlineUser);
                logger.info("添加新的session+用户名："+username);
            }
        }



    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        logger.info("---sessionCreated----");
        HttpSession session = event.getSession();
        /*if (session!=null){
            System.out.println(session.getId()+"session不为空！");
            if (onlineUserRepository.findBySessionid(session.getId())!=null){
            }
        }*/
        ServletContext application = session.getServletContext();
        // 在application范围由一个HashSet集保存所有的session
        HashSet sessions = (HashSet) application.getAttribute("sessions");
        if (sessions == null) {
            sessions = new HashSet();
            application.setAttribute("sessions", sessions);
        }
        // 新创建的session均添加到HashSet集中
       sessions.add(session);
        // 可以在别处从application范围中取出sessions集合
        // 然后使用sessions.size()获取当前活动的session数，即为“在线人数”
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException {
        logger.info("---sessionDestroyed----");
        HttpSession session = event.getSession();
        logger.info("deletedSessionId: "+session.getId());
        OnlineUser onlineUser=new OnlineUser();
        onlineUser=onlineUserRepository.findBySessionid(session.getId());
        if (onlineUser!=null){      //当session超时或被销毁时，从数据库中删除该sessionId。
            onlineUserRepository.delete(onlineUser.getId());
            logger.info("该session已经销毁！从onlineUser中删除记录！");
        }
        System.out.println(session.getCreationTime());
        System.out.println(session.getLastAccessedTime());
        ServletContext application = session.getServletContext();
        HashSet sessions = (HashSet) application.getAttribute("sessions");
        // 销毁的session均从HashSet集中移除
        sessions.remove(session);
    }

}
