package com.fengdonghao.server.service;

import com.fengdonghao.server.transition.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 16:03 2018/1/23
 */

public class ServerThread1 extends Thread{

    private static  String username;

    private static  String oder;

    private static String msg;


    Socket socket=null;
    public ServerThread1(Socket socket){
        this.socket=socket;
    }
String string;
    @Override
    public void run() {
        try {
            //获取客户端传过来的字符串
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            String line = "";
            while((line = br.readLine())!=null){
                string =line;
                System.out.println("我是服务器，客户端发来的信息是："+line);
            }
            socket.shutdownInput();
            //拆分字符串
            String[] s=string.split(":");
            username=s[0];
            oder=s[1];
            Tools tools=new Tools();
            if (tools.tool(username,oder).equals("success")){
                msg="success";
            }else{
                msg="error";
            }
            System.out.println(Thread.currentThread().getId()+" : "+Thread.currentThread().getName());
            //输出字符串 返回给客户端
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
            pw.write(msg);
            pw.flush();
            socket.shutdownOutput();//关闭socket的输出流
            System.out.println("return:"+msg);
            //关闭资源
            br.close();
            pw.close();
            socket.close();//注意：在使用socket进行TCP通信时，对于同一个Socket,如果关闭了输出流，
            //则与该输出流关联的socket也会被关闭，所以一般不用关闭流，直接关闭socket即可

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
