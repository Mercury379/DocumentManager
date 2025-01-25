package gui;

import console.AbstractUser;
import console.DataProcessing;
import console.Doc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;

public class DocControlFrame implements ActionListener {
    private JPanel panelDocControl;
    private JButton buttonSearch;
    private JButton buttonUpdate;
    private JButton buttonDownload;
    private JTable tableDoc;
    private AbstractUser User;
    private SearchDialog searchDialog;
    private DefaultTableModel tableModelDocs;
    private JFrame jFrame;
    private FileDialog saveFileDialog;
    static String uploadpath="D:\\面向对象与多线程\\uploadfile\\";
    static String downloadpath="D:\\面向对象与多线程\\downloadfile\\";

    public DocControlFrame(JFrame jFrame, AbstractUser User){
        this.User=User;
        this.jFrame=jFrame;
        jFrame.setTitle("档案管理页面");
        jFrame.setContentPane(panelDocControl);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setLocation(200,100);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);
        showDocList();
        if(User.getRole().equals("operator")){
            buttonUpdate.setEnabled(true);
            buttonDownload.addActionListener(this);
            buttonSearch.addActionListener(this);
            buttonUpdate.addActionListener(this);
        }else{
            buttonUpdate.setEnabled(false);
            buttonDownload.addActionListener(this);
            buttonSearch.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();
        if(command.equals(buttonSearch.getText())){
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        searchDialog=new SearchDialog();
                        searchDoc();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else if(command.equals(buttonUpdate.getText())){
            saveFileDialog=new FileDialog(jFrame,"上传档案对话框",FileDialog.LOAD);
            saveFileDialog.setVisible(true);
            UpdateDialog updateDialog=new UpdateDialog();
            if(updateDialog.getId().length()==0||updateDialog.getOther().length()==0){
                return;
            }else {
                uploadFile(updateDialog.getId(), updateDialog.getOther());
                JOptionPane.showMessageDialog(panelDocControl,"上传文件成功！");
            }
        }else if(command.equals(buttonDownload.getText())){
            try {
                int currentRow = tableDoc.getSelectedRow();
                Object object = tableModelDocs.getValueAt(currentRow, 3);
                String fileName = object.toString();
                String directory = "";
                DataProcessing.fileOperation(fileName, directory, false);
            }catch (Exception E){
                E.printStackTrace();
            }
            JOptionPane.showMessageDialog(panelDocControl,"下载文件成功！");
        }
        showDocList();
    }
    public void uploadFile(String id,String otherinfo){
        try {
            String directory=saveFileDialog.getDirectory();
            String fileName=saveFileDialog.getFile();
            DataProcessing.fileOperation(fileName,directory,true);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            DataProcessing.insertDoc(id,User.getName(),time,otherinfo,fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showDocList(){
        try{
            String[] colName={"档案号","创建者","时间","文件名","描述"};
            String[][] tableValue;
            Enumeration<Doc> docs= DataProcessing.listDoc();
            ArrayList<Doc> list=new ArrayList<>();
            while(docs.hasMoreElements()){
                Doc doc = docs.nextElement();
                list.add(doc);
            }
            int row=list.size();
            tableValue=new String[row][5];
            row=0;
            for(Doc doc:list){
                tableValue[row][0]=doc.getId();
                tableValue[row][1]=doc.getCreator();
                tableValue[row][2]=doc.getTimestamp().toString();
                tableValue[row][3]=doc.getFilename();
                tableValue[row][4]=doc.getDescription();
                row++;
            }
            tableModelDocs = new DefaultTableModel(tableValue,colName);
            tableDoc.setModel(tableModelDocs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void searchDoc(){
        try {
            String name = searchDialog.getName();
            Doc searchdoc = DataProcessing.searchDoc(name);
            if(searchdoc==null){
                JOptionPane.showMessageDialog(panelDocControl,"未找到该编号档案！");
            }
            int value = searchDialog.getDefaultCloseOperation();
            if (value == JDialog.DISPOSE_ON_CLOSE) {
                for (int i = 0; i < tableModelDocs.getRowCount(); i++) {
                    if (name.equals(tableModelDocs.getValueAt(i, 0))) {
                        tableDoc.getSelectionModel().setSelectionInterval(i, i);
                        Rectangle rectangle = tableDoc.getCellRect(i, 0, true);
                        tableDoc.scrollRectToVisible(rectangle);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
