package gui;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class UserControlFrame {
    private JPanel panelUserControl;
    private JTable tableUser;
    private JButton buttonSearch;
    private JButton buttonNew;
    private JButton buttonChange;
    private JButton butttonDelete;
    private AbstractUser User;
    private DefaultTableModel tableModelUsers;
    private SearchDialog searchDialog;

    public UserControlFrame(JFrame jFrame, AbstractUser User){
        this.User=User;
        jFrame.setTitle("用户管理页面");
        jFrame.setContentPane(panelUserControl);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        showFileList();
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            searchDialog=new SearchDialog();
                            searchUser();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        buttonNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    NewDialog newDialog=new NewDialog();
                    AbstractUser User= newDialog.getUser();
                    DataProcessing.insertUser(User.getName(), User.getPassword(), User.getRole());
                    showFileList();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        butttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int currentRow = tableUser.getSelectedRow();
                    Object object = tableModelUsers.getValueAt(currentRow, 0);
                    String username = object.toString();
                    DataProcessing.deleteUser(username);
                    showFileList();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int currentRow = tableUser.getSelectedRow();
                    Object object = tableModelUsers.getValueAt(currentRow, 0);
                    String username = object.toString();
                    AbstractUser updateUser=DataProcessing.searchUser(username);
                    ChangeDialog changeDialog=new ChangeDialog(updateUser);
                    showFileList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void showFileList(){
        try{
            String[] colName={"用户名","密码","角色"};
            String[][] tableValue;
            Enumeration<AbstractUser> users= DataProcessing.listUser();
            ArrayList<AbstractUser> list=new ArrayList<>();
            while(users.hasMoreElements()){
                AbstractUser user = users.nextElement();
                list.add(user);
            }
            int row=list.size();
            tableValue=new String[row][3];
            row=0;
            for(AbstractUser user:list){
                tableValue[row][0]=user.getName();
                tableValue[row][1]=user.getPassword();
                tableValue[row][2]=user.getRole();
                row++;
            }
            tableModelUsers = new DefaultTableModel(tableValue,colName);
            tableUser.setModel(tableModelUsers);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void searchUser(){
        try {
            String name = searchDialog.getName();
            AbstractUser searchuser = DataProcessing.searchUser(name);
            if(searchuser==null){
                JOptionPane.showMessageDialog(panelUserControl,"找不到该用户");
            }
            int value = searchDialog.getDefaultCloseOperation();
            if (value == JDialog.DISPOSE_ON_CLOSE) {
                for (int i = 0; i < tableModelUsers.getRowCount(); i++) {
                    if (name.equals(tableModelUsers.getValueAt(i, 0))) {
                        tableUser.getSelectionModel().setSelectionInterval(i, i);
                        Rectangle rectangle = tableUser.getCellRect(i, 0, true);
                        tableUser.scrollRectToVisible(rectangle);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
