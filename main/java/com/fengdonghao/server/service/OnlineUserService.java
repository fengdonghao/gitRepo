package com.fengdonghao.server.service;

import com.fengdonghao.server.bean.UserInfo;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 11:15 2018/1/18
 */
@Service
public class OnlineUserService {
    public  int getMaxUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        ServletContext application = session.getServletContext();
        HashSet sessions=(HashSet)application.getAttribute("sessions");
        int countUser=0;
        try{
             countUser = sessions.size();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        System.out.println("###################当前在线人数:"+sessions.size());
        for (Object s:sessions){
                System.out.println(s);
                System.out.println(sessions);
        }

     /*   String onlineuser = "";
        int countOnlineUser=0 ;
        for(Iterator it = (Iterator) sessions.iterator(); it.hasNext();){
            HttpSession se = (HttpSession) it.next();
            if(se!=null){
                UserInfo ui = (UserInfo) se.getAttribute("username");
                if(ui!=null){
                    onlineuser += "___"+ui.getName();
                    countOnlineUser++;
                }
            }
        }*/
//        System.out.println("countOnlineUser:"+countOnlineUser);
//        System.out.println("###################当前登录用户:"+onlineuser);
//        return flag==1?countUser:countOnlineUser;
        return countUser;
    }
}
