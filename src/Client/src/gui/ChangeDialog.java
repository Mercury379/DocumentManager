package gui;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ChangeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton Button;
    private JTextField textField1;
    private JComboBox comboBox1;
    private AbstractUser User;

    public ChangeDialog(AbstractUser user) {
        User=user;
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
        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.setPassword("123");
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
        textField1.setText(User.getName());
        if(User.getRole().equals("administrator")){
            comboBox1.setSelectedIndex(0);
        }else if(User.getRole().equals("operator")){
            comboBox1.setSelectedIndex(1);
        }else{
            comboBox1.setSelectedIndex(2);
        }
        setVisible(true);
    }

    private void onOK() {
        // add your code here
        int role=comboBox1.getSelectedIndex();
        switch (role){
            case 0:
                User.setRole("administrator");
                break;
            case 1:
                User.setRole("operator");
                break;
            case 2:
                User.setRole("browser");
                break;
            default:
        }
        try {
            DataProcessing.updateUser(User.getName(),User.getPassword(),User.getRole());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
