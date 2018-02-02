package com.fengdonghao.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author:fdh
 * @Description: 对文件的路径进行配置,创建一个虚拟路径/images/**
 * @Date： Create in 14:08 2017/12/20
 */
@Configuration
public class MyWebAppConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       /* if(mImagesPath.equals("") || mImagesPath.equals("${cbs.imagesPath}")){
            String imagesPath = MyWebAppConfiguration.class.getClassLoader().getResource("").getPath();
            if(imagesPath.indexOf(".jar")>0){
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            }else if(imagesPath.indexOf("classes")>0){
                imagesPath = "file:"+imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/"))+"/images/";
            mImagesPath = imagesPath;
        }*/
        /*LoggerFactory.getLogger(MyWebAppConfiguration.class).info("加载路径配置文件:"+"imagesPath="+mImagesPath);*/
        registry.addResourceHandler("/**").addResourceLocations("file:/E:/WebPackage/IdeaProjects/server/src/main/resources/static/");
        registry.addResourceHandler("/js/**").addResourceLocations("file:/E:/WebPackage/IdeaProjects/server/src/main/resources/static/js");
        registry.addResourceHandler("/util/**").addResourceLocations("file:/E:/WebPackage/IdeaProjects/server/src/main/resources/static/");
        registry.addResourceHandler("/system/**").addResourceLocations("file:/E:/WebPackage/IdeaProjects/server/src/main/resources/static/system/");

        super.addResourceHandlers(registry);
    }
}
