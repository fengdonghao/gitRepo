package com.fengdonghao.server.controller;

import com.fengdonghao.server.bean.UserInfo;
import com.fengdonghao.server.config.LogoConfig;
import com.fengdonghao.server.service.CoderService;
import com.fengdonghao.server.util.MatrixToImageWriter1;
import com.fengdonghao.server.util.TwoDimension;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 9:01 2018/1/31
 */
@Controller
@RequestMapping("/util")
public class UtilController {
    @Autowired
    private LogoConfig logoConfig;
    @Autowired
    private CoderService coderService;

    @RequestMapping("11")
    public void getTwoDimension(HttpServletRequest request, HttpServletResponse response){

        TwoDimension twoDimension=new TwoDimension();
//        MatrixToImageWriter1 matrixToImageWriter=new MatrixToImageWriter1();
        try {
//            matrixToImageWriter.createRqCode("https://www.baidu.com" , 300, 300, response.getOutputStream());
            twoDimension.getTwoDimension("https://www.baidu.com",response,400,400);

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @PostMapping("getTwoDimension")
    public String  watermark(ModelMap map,String msg) throws Exception {
        String content=msg;
        String str="";
        String contents="";
        if (!content.contains("https://")||!content.contains("http://")){
            String[] strings=content.split("&");

            for (int i=0;i<strings.length;i++){
                str=str+strings[i]+"\n";
            }
             contents=str;
        }else{
            contents=msg;
        }
        Subject currentUser= SecurityUtils.getSubject();
        UserInfo userInfo=(UserInfo)currentUser.getPrincipal();
        String username=userInfo.getUsername();
        System.out.println(username);
        String realUploadPath = "E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static" ;
        String imageName = username+".jpg" ;
        // 模拟订单详情
        coderService.encode(contents, 300, 300,realUploadPath, imageName);
        String picName=logoConfig.LogoMatrix(realUploadPath, imageName) ;
        System.out.println(picName);
        map.addAttribute("username","logo_"+username);
        return "TwoDimension";
    }
    @GetMapping("preTwoDimension")
    public String preTwoDismension(){
        return "PreTwoDimension";
    }


}
