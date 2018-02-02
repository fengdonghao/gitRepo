package com.fengdonghao.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 9:25 2018/1/23
 */
@Service
public class SaveHeadPic {

    public String saveHeadPic(MultipartFile file, String name) throws IOException {
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out1 = new BufferedOutputStream(new FileOutputStream(new File("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\" + name + ".jpg")));
                out1.write(file.getBytes());
                out1.flush();
                out1.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream input1 = null;
            OutputStream output1 = null;
            try {
                input1 = new FileInputStream("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\noHeadPic.jpg");
                output1 = new FileOutputStream("E:\\WebPackage\\IdeaProjects\\server\\src\\main\\resources\\static\\" + name + ".jpg");
                byte[] buf1 = new byte[1024];
                int bytesRead;
                while ((bytesRead = input1.read(buf1)) > 0) {
                    output1.write(buf1, 0, bytesRead);
                }
            } finally {
                input1.close();
                output1.close();
            }

        }
        return "success";
    }

}