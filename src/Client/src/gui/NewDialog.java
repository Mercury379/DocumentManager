package gui;

import console.AbstractUser;
import console.Administrator;
import console.Browser;
import console.Operator;

import javax.swing.*;
import java.awt.event.*;

public class NewDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField passwordTextFeild;
    private JComboBox comboBox1;
    private JTextField nameTextFeild;
    private AbstractUser User;

    public AbstractUser getUser() {
        return User;
    }


    public NewDialog() {
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
        setTitle("新增用户");
        setLocation(300,200);
        pack();
        setVisible(true);
    }

    private void onOK() {
        String name=nameTextFeild.getText();
        String password=passwordTextFeild.getText();
        if(name.length()==0||password.length()==0){
            JOptionPane.showMessageDialog(contentPane,"输入不能为空！");
        }else{
            switch (comboBox1.getSelectedIndex()){
                case 0:
                    User = new Administrator(name,password,"administrator");
                    break;
                case 1:
                    User=new Operator(name,password,"operator");
                    break;
                case 2:
                    User =new Browser(name,password,"browser");
                    break;
                default:
            }
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
