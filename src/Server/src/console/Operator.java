package console;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Operator extends AbstractUser {
    public Operator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() throws IOException, SQLException {
        Scanner scan=new Scanner(System.in);

        int flag=1;
        String id;
        String description;
        String directory;
        String filename;
        String password;

        while(flag==1) {
            System.out.println("****欢迎进入档案录入员菜单****");
            System.out.println("    1、上传档案");
            System.out.println("    2、下载档案");
            System.out.println("    3、档案列表");
            System.out.println("    4、修改密码");
            System.out.println("    5、退出");
            System.out.println("*************************");
            System.out.println("请选择菜单：");    //operator菜单
            int order = scan.nextInt();
            switch (order) {
                case 1:
                    System.out.println("请输入上传档案路径：");
                    directory=scan.next();
                    System.out.println("请输入上传档案名称：");
                    filename = scan.next();
                    System.out.println("请输入上传档案id：");
                    id=scan.next();
                    System.out.println("请输入上传档案描述：");
                    description=scan.next();
                    if(updateFile(directory,filename)==true){
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        System.out.println("上传档案成功！");
                        DataProcessing.insertDoc(id,super.getName(),time,description,filename);
                    }else{
                        System.out.println("上传档案失败");
                    }
                    break;
                case 2:
                    System.out.println("请输入下载档案id：");
                    id=scan.next();
                    if(DataProcessing.searchDoc(id)==null){
                        System.out.println("下载档案不存在！");
                        break;
                    }
                    if(downloadFile(id)==true){
                        System.out.println("下载档案成功！");
                    }else{
                        System.out.println("下载档案失败！");
                    }
                    break;
                case 3:
                    showFileList();
                    break;
                case 4:
                    System.out.println("请输入你要修改的密码：");
                    password = scan.next();
                    changeSelfInfo(password);
                    break;
                case 5:
                    flag=0;
                    break;
                default:
                    System.out.println("命令不在菜单内！请重新输入！");
                    break;
            }
        }
    }
}
