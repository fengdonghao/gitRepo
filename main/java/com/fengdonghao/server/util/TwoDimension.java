package com.fengdonghao.server.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 8:56 2018/1/31
 */
public class TwoDimension {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TwoDimension.class);

    public void getTwoDimension(String url, HttpServletResponse response, int width, int height) throws IOException {
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                stream = response.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (WriterException e) {
                e.printStackTrace();
                logger.error("生成二维码失败!");
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }
}