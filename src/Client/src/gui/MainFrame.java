package gui;

import console.AbstractUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame implements ActionListener {
    private JPanel panelMain;
    private JButton buttonUserControl;
    private JButton buttonDocControl;
    private JButton buttonUserInfo;
    private JButton buttonExit;
    private JFrame jFrame;
    private AbstractUser User;

    public MainFrame(JFrame jFrame, AbstractUser User){
        this.jFrame=jFrame;
        jFrame.setTitle("主菜单页面");
        jFrame.setContentPane(panelMain);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        this.User=User;
        if(User.getRole().equals("administrator")){
            buttonDocControl.addActionListener(this);
            buttonUserInfo.addActionListener(this);
            buttonExit.addActionListener(this);
            buttonUserControl.addActionListener(this);
        }else{
            buttonUserControl.setEnabled(false);
            buttonExit.addActionListener(this);
            buttonDocControl.addActionListener(this);
            buttonUserInfo.addActionListener(this);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("退出登录")){
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
            jFrame.setVisible(false);
        }else if(command.equals("用户管理")){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        UserControlFrame usercontrolFrame = new UserControlFrame(new JFrame(),User);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else if(command.equals("档案管理")){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        DocControlFrame doccontrolFrame = new DocControlFrame(new JFrame(),User);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else if(command.equals("个人中心")){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        UserInfoFrame userInfoFrame = new UserInfoFrame(new JFrame(),User);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
