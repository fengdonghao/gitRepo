package com.fengdonghao.server.service;


import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 17:58 2018/1/24
 */
@Service
public class SendMessageDemo{
    public static final int PORT = 6666;//监听的端口号



    public void init() {
        System.out.println("sever begins");
        SendMessageDemo server = new SendMessageDemo();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket client = serverSocket.accept();
                //一个客户端连接就开两个线程分别处理读和写
                new Thread(new ReadHandlerThread(client)).start();
//                new Thread(new WriteHandlerThread(client)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 *处理读操作的线程
 */
class ReadHandlerThread implements Runnable{
    private Socket client=null;

    public ReadHandlerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try{
            while(true){
                //读取客户端数据

                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("客户端说:" + in.readLine());

                System.out.println(client.getLocalAddress());
                System.out.println(client.getInetAddress());
                System.out.println(Thread.currentThread().getId());
                System.out.println(Thread.currentThread().getName());
                new Thread(new WriteHandlerThread(client)).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(client != null){
                client = null;
            }
        }
    }
}

/*
 * 处理写操作的线程
 */
class WriteHandlerThread implements Runnable{
    private Socket client;

    public WriteHandlerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("start");
        PrintWriter out=null;
        try {
             out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(),"UTF-8"),true);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));//从控制台获取输入的内容

           /* while(true){
                //向客户端回复信息
//                System.out.println("请输入：");
//                out.println(reader.readLine());
                out.write("sssss");
                out.flush();
            }*/
            System.out.println("返回一个结果！");
            out.write("sssss");
            out.flush();


    }

}