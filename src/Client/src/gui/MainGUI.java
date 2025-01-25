package gui;

import console.DataProcessing;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainGUI {
    public  static void main(String[] args){
        DataProcessing.connectToDatabase();
        DataProcessing.connectToServer();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    LoginFrame loginFrame = new LoginFrame(new JFrame());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                //在此处添加退出应用程序前需完成工作，如：关闭网络连接、关闭数据库连接等
                DataProcessing.disconnectFromDataBase();
                System.out.println("应用程序退出！");
            }
        });
    }
}
