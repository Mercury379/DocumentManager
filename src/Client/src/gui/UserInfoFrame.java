package gui;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserInfoFrame {
    private JPanel panelUserInfo;
    private JTextField textField;
    private JTextField passwordField;
    private JComboBox comboBox;
    private JButton buttonChange;
    private JButton buttonExit;
    private AbstractUser User;
    private String newpassword;

    public UserInfoFrame(JFrame jFrame, AbstractUser User){
        this.User=User;
        jFrame.setTitle("个人中心页面");
        jFrame.setContentPane(panelUserInfo);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        AbstractUser user;
        textField.setText(User.getName());
        try {
            user = DataProcessing.searchUser(User.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        passwordField.setText(user.getPassword());
        if(user.getRole().equals("administrator")){
            comboBox.setSelectedIndex(0);
        }else if(user.getRole().equals("operator")){
            comboBox.setSelectedIndex(1);
        }else{
            comboBox.setSelectedIndex(2);
        }
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
            }
        });
        buttonChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            int role=comboBox.getSelectedIndex();
                            switch (role){
                                case 0:
                                    user.setRole("administrator");
                                    break;
                                case 1:
                                    user.setRole("operator");
                                    break;
                                case 2:
                                    user.setRole("browser");
                                    break;
                                default:
                            }
                            temp changeDialog = new temp();
                            newpassword=changeDialog.getSurepassword();
                            if(changeDialog.getSurepassword().length()==0||changeDialog.getNewpassword().length()==0||changeDialog.getOldpassword().length()==0){
                                JOptionPane.showMessageDialog(panelUserInfo,"输入不能为空！");
                            } else if(DataProcessing.searchUser(user.getName(),changeDialog.getOldpassword())==null){
                                JOptionPane.showMessageDialog(panelUserInfo,"输入密码错误！");
                            } else{
                                DataProcessing.updateUser(user.getName(),newpassword,user.getRole());
                                JOptionPane.showMessageDialog(panelUserInfo,"修改密码成功！");
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
