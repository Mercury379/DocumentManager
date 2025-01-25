package gui;

import javax.swing.*;
import java.awt.event.*;

public class temp extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;
    private String oldpassword;
    private String newpassword;
    private String surepassword;

    public String getSurepassword() {
        return surepassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public temp() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setTitle("修改用户");
        setLocation(300,200);
        pack();
        setVisible(true);
    }


    private void onOK() {
        // add your code here
        oldpassword=passwordField1.getText();
        if(passwordField2.getText().equals(passwordField3.getText())){
            newpassword=passwordField2.getText();
            surepassword=passwordField3.getText();
        }else{
            JOptionPane.showMessageDialog(contentPane,"修改密码与确认密码不一致！");
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
