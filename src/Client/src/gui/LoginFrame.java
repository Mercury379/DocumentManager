package gui;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame implements ActionListener {
    private JPanel panelLogin;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton buttonLoad;
    private JButton buttonExit;
    private JFrame jFrame;
    private AbstractUser User;

    public LoginFrame(JFrame jFrame){
        this.jFrame=jFrame;
        jFrame.setTitle("登录页面");
        jFrame.setContentPane(panelLogin);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        buttonLoad.addActionListener(this);
        buttonExit.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name=textField.getText();
            String password= passwordField.getText();
            if (e.getActionCommand() == buttonLoad.getText()) {
                if (DataProcessing.searchUser(name, password) == null) {
                    JOptionPane.showMessageDialog(panelLogin,"输入用户名或用户密码错误！");
                } else {
                    User=DataProcessing.searchUser(name,password);
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                MainFrame mainFrame = new MainFrame(new JFrame(), User);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    jFrame.setVisible(false);
                }
            } else {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        //在此处添加退出应用程序前需完成工作，如：关闭网络连接、关闭数据库连接等
                        System.out.println("应用程序退出！");
                    }
                });
                System.exit(0);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
