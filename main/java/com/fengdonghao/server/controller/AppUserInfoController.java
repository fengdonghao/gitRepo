package com.fengdonghao.server.controller;

import com.fengdonghao.server.bean.Result;
import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.repository.OnlineUserRepository;
import com.fengdonghao.server.repository.UserInfoRepository;
import com.fengdonghao.server.service.*;
import com.fengdonghao.server.service.ServerThread1;
import com.fengdonghao.server.util.ResultUtil;
import org.apache.catalina.security.SecurityUtil;
import org.apache.http.protocol.HTTP;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import static com.fengdonghao.server.service.Encrypt.decryptBasedDes;
import static com.fengdonghao.server.service.Encrypt.encryptBasedDes;

/**
 * @ Author:fdh
 * @ Description: APP客户端的请求
 * @ Date： Create in 9:05 2018/1/18
 */
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppUserInfoController {


    @Autowired
    private SendMessageDemo sendMessageDemo;

    @Autowired
    private ServerSocket1 serverSocket1;


    @Autowired
    private SaveHeadPic saveHeadPic;

    Semaphore semaphore=new Semaphore(1); //并发总资源数

    @Autowired
    private OnlineUserRepository onlineUserRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public final  static Logger logger= LoggerFactory.getLogger(AppUserInfoController.class);

    @PostMapping("/userInfoAdd")
    public Object AppUserInfoAdd(UserInfo userInfo, @RequestParam("file")  MultipartFile file) throws IOException,InterruptedException{
        String tmpName=userInfo.getUsername();
        UserInfo userInfoTmp=userInfoRepository.findByUsername(tmpName);  //在数据库中查找当前要添加的用户，如果存在。。。
        if (userInfoTmp!=null){
            logger.info("username already exist！");
            return ResultUtil.error(102,"username already exist！");
        }
        if (saveHeadPic.saveHeadPic(file,tmpName).equals("success")){
            String fileName1="E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\"+tmpName+".jpg";
            userInfo.setHeadPath(fileName1);
        }
        userInfo.setUsername(userInfo.getUsername());
        userInfo.setName(userInfo.getName());
        userInfo.setPassword(encryptBasedDes(userInfo.getPassword()));
        int availablePermits=semaphore.availablePermits();
        if(availablePermits>0){
            System.out.println("：抢到资源");
        }else{
            System.out.println("资源已被占用，稍后再试");
            return ResultUtil.error(108,"resource is busy now!");
        }try{
            semaphore.acquire(1);
//            Thread.currentThread().sleep(60000);
            userInfoRepository.save(userInfo);
        }finally{
            semaphore.release(1);
        }
        UserInfo userInfo1=new UserInfo();
        userInfo1=userInfoRepository.findByUsername(tmpName);
        logger.info("被添加的 userNub is:"+userInfo1.getUid()+"; username is:"+tmpName);
        return ResultUtil.success1();

    }


    @PostMapping("login")
    public Object login(UserInfo userInfo, HttpServletRequest request)throws IOException{
        String temUsername=userInfo.getUsername();
        String temPassword=userInfo.getPassword();
        logger.info("登录名：tmpUsername: "+temUsername);
        logger.info("登录密码：temPassword: "+temPassword);
        UserInfo userInfo1=new UserInfo();
        userInfo1=userInfoRepository.findByUsername(temUsername);
        if (userInfo1==null){
            logger.info("数据库中没有该用户的信息！");
            return ResultUtil.error(100,"数据库中没有该用户的信息！"); //表示用户不存在
        }
        if (temPassword.equals(decryptBasedDes(userInfo1.getPassword()))){
            logger.info("密码 验证 success");
            HttpSession session=request.getSession(true);//true表示如果此时有session，则获取session，如果没有session，则新创建一个session
            session.setMaxInactiveInterval(60);
            session.setAttribute("username",temUsername); //自定义session
            logger.info("session最长存活期："+session.getMaxInactiveInterval());
            if (request.getSession(false)!=null){   //如果session不存在（被销毁了），则说明 user is online
                logger.info("------当前用户第一次登录，添加属性---"+session.getId());
                session.setAttribute("password",temPassword);
            }else{
                logger.info("user id already online!");
                return ResultUtil.error(107,"user id already online!");
            }
            /*OnlineUser onlineUser=new OnlineUser();
            onlineUser.setUsername(temUsername);
            onlineUser.setSessionId(sessionId);
            onlineUserRepository.save(onlineUser);*/
            UserInfo userInfo2=new UserInfo();
            userInfo2.setUsername(temUsername);
            logger.info("登录成功！");
            return ResultUtil.success(userInfo2);
        }else{
            logger.info("密码 验证 failed!");
            return ResultUtil.error(101,"password error");//密码错误
        }
    }


    @GetMapping("test")
    public String test(HttpServletRequest request){

        if (request.getSession(false)==null){
            return "noSession";
        }
        HttpSession session=request.getSession();
        System.out.println(session.getId());
        return "test";
    }

    @GetMapping("/logout")
    public Object logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        System.out.println(session.getId());
        session.invalidate();
        return ResultUtil.success1();
    }

    @PostMapping("/editHead")
    public Object editHead(HttpServletRequest request,@RequestParam("file") MultipartFile file) throws IOException{
        if (request.getSession(false)==null){
            return ResultUtil.error(104,"noSession relogin!");
        }
        HttpSession session=request.getSession();
        String temUsername=(String)session.getAttribute("username");
        System.out.println(temUsername);
        if (saveHeadPic.saveHeadPic(file,temUsername).equals("success")){
            String fileName1="E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\"+temUsername+".jpg";
            userInfoRepository.updateHeadByUsername(fileName1,temUsername);
            logger.info(temUsername+" headPath更改 成功！");
        }
        return ResultUtil.success("username:"+temUsername);

    }

    @PostMapping("/editInfo")
    public Result<UserInfo> edit( HttpServletRequest request, UserInfo userInfo) throws IOException{
        if (request.getSession(false)==null){
            return ResultUtil.error(104,"noSession relogin!");
        }
        String temUsername=userInfo.getUsername();
        String temPassword=userInfo.getPassword();
        UserInfo userInfo1=new UserInfo();
        userInfo1=userInfoRepository.findByUsername(temUsername);
        Long id=userInfo1.getUid();
        System.out.println("要修改的ID是："+id);
        userInfo1.setPassword(encryptBasedDes(temPassword));
        userInfo1.setUid(id);
        userInfo1.setUsername(temUsername);
        userInfoRepository.save(userInfo1);

        UserInfo userInfo2=new UserInfo();
        userInfo2.setUsername(temUsername);
        userInfo2.setPassword(temPassword);
        return ResultUtil.success(userInfo2);
    }

    @GetMapping("/test1")
    public String test1(){
        serverSocket1.ServerSocketDemo();

        return "success";
    }

    @GetMapping("/test2")
    public String test2(){
        Socket socket=new Socket();
        new ServerThread1(socket).start();
        return "success";
    }

    @GetMapping("/test4")
    public String test4() throws  IOException{
        System.out.println("scheduled....");
        sendMessageDemo.init();

        return "success";
    }
}
