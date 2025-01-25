package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Browser extends AbstractUser {
    public Browser(String name, String password, String role) {
        super(name,password,role);
    }

    @Override
    public void showMenu() throws IOException, SQLException {

        Scanner scan=new Scanner(System.in);

        String id;
        String password;

        int flag=1;
        while(flag==1) {
            System.out.println("****欢迎进入档案浏览员菜单****");
            System.out.println("    1、下载档案");
            System.out.println("    2、档案列表");
            System.out.println("    3、修改密码");
            System.out.println("    4、退出");
            System.out.println("*************************");
            System.out.println("请选择菜单：");     //browser菜单
            int order = scan.nextInt();
            switch (order) {
                case 1:
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
                case 2:
                    showFileList();
                    break;
                case 3:
                    System.out.println("请输入你要修改的密码：");
                    password = scan.next();
                    changeSelfInfo(password);
                    break;
                case 4:
                    flag=0;
                    System.out.println("系统退出, 谢谢使用 ! ");
                    break;
                default:
                    System.out.println("命令不在菜单内！请重新输入！");
                    break;
            }
        }
    }
}
