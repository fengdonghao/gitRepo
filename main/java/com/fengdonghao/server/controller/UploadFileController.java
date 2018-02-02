package com.fengdonghao.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * @Author:fdh
 * @Description:
 * @Date： Create in 9:33 2017/12/20
 */
@Controller
public class UploadFileController {

    private static final Logger logger= LoggerFactory.getLogger(UploadFileController.class);
    @GetMapping("uploadFile")
    public String upload1(){
        return "uploadHead";
    }
    @PostMapping("uploadFile")
    @ResponseBody
    public String upload2(@RequestParam("file")  MultipartFile file){



        String fileName=file.getOriginalFilename();
        logger.info("上传的文件名为："+fileName);
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        String filePath="E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\";

        File dest=new File(filePath+fileName);
        try {
            file.transferTo(dest);
            return "success";
        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }catch (IllegalStateException e){
            e.printStackTrace();
            return "error";
        }





    }
}
