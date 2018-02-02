package com.fengdonghao.server.config;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ Author:fdh
 * @ Description:添加logo给二维码
 * @ Date： Create in 10:24 2018/1/31
 */
@Configuration
public class LogoConfig {
    public String LogoMatrix(String realUploadPath, String imgPath) {
        /**
         * 读取二维码图片，并构建绘图对象
         */
        OutputStream os = null ;
        String logoFileName = "logo_"+imgPath ;
        try {
            Image image2 = ImageIO.read(new File(realUploadPath+"/"+"new-"+imgPath)) ;
            int width = image2.getWidth(null) ;
            int height = image2.getHeight(null) ;
            BufferedImage bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) ;
            //BufferedImage bufferImage =ImageIO.read(image) ;
            Graphics2D g2 = bufferImage.createGraphics();
            g2.drawImage(image2, 0, 0, width, height, null) ;
            int matrixWidth = bufferImage.getWidth();
            int matrixHeigh = bufferImage.getHeight();

            //读取Logo图片
            BufferedImage logo= ImageIO.read(new File(realUploadPath+"/"+imgPath));
            //开始绘制图片
            g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
            BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke);// 设置笔画对象
            //指定弧度的圆角矩形
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
            g2.setColor(Color.white);
            g2.draw(round);// 绘制圆弧矩形

            //设置logo 有一道灰色边框
            BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
            g2.setStroke(stroke2);// 设置笔画对象
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
            g2.setColor(new Color(128,128,128));
            g2.draw(round2);// 绘制圆弧矩形

            g2.dispose();

            bufferImage.flush() ;
            os = new FileOutputStream(realUploadPath+"/"+logoFileName) ;
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os) ;
            en.encode(bufferImage) ;
            System.out.println(" success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return logoFileName;
    }
}
