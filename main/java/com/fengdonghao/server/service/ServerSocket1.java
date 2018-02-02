package com.fengdonghao.server.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * @ Author:fdh
 * @ Description:
 * @ Date： Create in 15:53 2018/1/23
 */
@Service
public class ServerSocket1 {

    ServerSocket serverSocket;

    public void ServerSocketDemo(){
    int count=0;
    try{
        serverSocket=new ServerSocket(5555);
        System.out.println("服务端已经启动，等待客户端连接");
        while (true){
            Socket socket=serverSocket.accept();
            new ServerThread1(socket).start();
            count++;
            System.out.println("客户端的IP："+socket.getInetAddress().getHostAddress());
            System.out.println("连接的客户端的总数"+count);
        }

    }catch (IOException exception){
        exception.printStackTrace();
    }
    }

}
