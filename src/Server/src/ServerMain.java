import console.DataProcessing;
import gui.LoginFrame;
import service.SocketService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
class Threads extends Thread {
    private static int countConnect=0;
    Socket socket;
    public Threads(Socket socket) {
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            int id=countConnect++;
            System.out.println("与客户端"+id+"建立连接！");
            System.out.println("连接成功！");
            SocketService socketService= null;
            socketService = new SocketService(socket,id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ServerMain {
    private static ServerSocket server;
 //   private static int countConnect=0;

    public static void main(String[] args) {
        System.out.println("启动>>>>>>>");
        DataProcessing.connectToDatabase();
        startServer();
    }
    private static void startServer(){
        try {
            System.out.println("主服务器启动>>>>>>>");
            server=new ServerSocket(12345,100);
            while(true){
                Socket socket=server.accept();
                Threads mythread=new Threads(socket);
                mythread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
