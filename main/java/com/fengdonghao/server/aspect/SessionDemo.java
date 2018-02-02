package com.fengdonghao.server.aspect;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 14:43 2018/1/17
 */

public class SessionDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();
        session.setAttribute("name","fengdh");
        String sessionId=session.getId();
        if (session.isNew()){
            System.out.println("session创建成功，session的id是："+sessionId);
        }else{
            System.out.println("服务器已经存在该session了，session的id是："+sessionId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
