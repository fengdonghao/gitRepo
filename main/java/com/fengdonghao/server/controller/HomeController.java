package com.fengdonghao.server.controller;

import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.repository.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@Controller

public class HomeController {


    private int state=1;

    @Autowired
    private  UserInfoRepository userInfoRepository;
    @Autowired
    private UserInfoService userInfoService;
    private UserInfo userInfo;


    @RequestMapping(value = {"/index"})
    public String index(){
        return "index";
    }

    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login(){
        return "login";
    }
    // 登录提交地址和applicationontext-server.xml配置的loginurl一致。 (配置文件方式的说法)
    @RequestMapping(value= "/login",method = RequestMethod.POST)
   public String login(HttpServletRequest request, Map<String, Object> map,UserInfo userInfo) throws Exception {


        System.out.println("登录失败"+"HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        /*request.getSession().setAttribute("username",userInfo);*/

        System.out.println("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else if (LockedAccountException.class.getName().equals(exception)){
                msg="该账号已登录！";
                System.out.println("LockedAccountException。。。该账号已登录！");
            }else{
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
        }

        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理.
        return "/login";
    }

    @RequestMapping("list")
    public String list(){
        return "list";
    }

    @RequestMapping(value = "top")
    public String top(Map<String,Object>map){

        map.put("time",new Date());
        Subject currentUser=SecurityUtils.getSubject();
        Session session=currentUser.getSession();
       /*if (session.getAttribute("state")==null){
           System.out.println("state为空！");
           session.setAttribute("state",1);


       }else{
           int state1=(int)session.getAttribute("state");
           System.out.println("state: "+state1);

           return "redirect:login";
            session.getAttribute("state");
        System.out.println(session.getAttribute("state"));
       }*/
     /*   Session session=currentUser.getSession();
         System.out.println(session.getId()+"  1");
        System.out.println(session.getHost()+"  2");
        System.out.println(session.getTimeout()+"  3");
        System.out.println(session.getAttributeKeys()+"  4");
        String string=String.valueOf(session.getAttribute("username"));
        System.out.println(string);
        UserInfo userInfo=(UserInfo)currentUser.getPrincipal();
        String username=userInfo.getUsername();
        System.out.println("username："+username);
      if (username!=null){
            userInfoRepository.updateStatusByUsername(0,username);
            System.out.println("用户state更改成功！");
        }*/
        map.put("user1",currentUser.getPrincipal());
        return "top";
    }
    @RequestMapping(value = "menu")
    public String menu(){
        return "menu";
    }
    @GetMapping("403")
    public String noPermisson(){
        return "403";
    }
    @GetMapping("logout")
    public String  logOut(HttpSession session){
       /* ServletContext context = session.getServletContext();
        int lineCount = (Integer) context.getAttribute("lineCount");
        context.setAttribute("lineCount", lineCount - 1);
        HashSet<HttpSession> sessionSet = (HashSet<HttpSession>) context.getAttribute("sessionSet");
        if(sessionSet!=null){
            sessionSet.remove(session);
        }*/
        Subject currentUser = SecurityUtils.getSubject();
        UserInfo o=(UserInfo)currentUser.getPrincipal();
       /* System.out.println(o);
        userInfoRepository.updateStatusById(0,o.getUid());//退出时，把用户的状态改为0
        System.out.println("state修改o");*/
        currentUser.logout();
        return "logout";
    }





}
