package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Scanner;

public class Administrator extends AbstractUser {
    public Administrator(String name, String password, String role) {
        super(name,password,role);
    }

    @Override
    public void showMenu() throws SQLException, IOException {

        Scanner scan=new Scanner(System.in);
        String id;
        String name;
        String password;
        String role;

        int flag=1;
        while(flag==1) {
            System.out.println("****欢迎进入档案管理员菜单****");
            System.out.println("    1、新增用户");
            System.out.println("    2、删除用户");
            System.out.println("    3、修改用户");
            System.out.println("    4、用户列表");
            System.out.println("    5、下载档案");
            System.out.println("    6、档案列表");
            System.out.println("    7、修改密码");
            System.out.println("    8、退出");
            System.out.println("*************************");
            System.out.println("请选择菜单：");      //administrator菜单
            int order = scan.nextInt();
            switch (order) {
                case 1:
                    System.out.println("请输入姓名：");
                    name=scan.next();
                    System.out.println("请输入密码：");
                    password=scan.next();
                    System.out.println("请输入身份：");
                    role=scan.next();
                    if(DataProcessing.insertUser(name,password,role)){   //判断新增用户是否已在哈希表中
                        System.out.println("新增用户成功！");
                    }else{
                        System.out.println("新增用户失败！");
                    }
                    break;
                case 2:
                    System.out.println("请输入姓名：");
                    name=scan.next();
                    if(DataProcessing.deleteUser(name)) {    //判断删除用户是否在哈希表中
                        System.out.println("删除用户成功！");
                    }else{
                        System.out.println("删除用户失败！");
                    }
                    break;
                case 3:
                    System.out.println("请输入姓名：");
                    name=scan.next();
                    System.out.println("请输入密码：");
                    password=scan.next();
                    System.out.println("请输入身份：");
                    role=scan.next();
                    if(DataProcessing.updateUser(name,password,role)){   //判断修改用户是否在哈希表中
                        System.out.println("修改用户成功！");
                    }else{
                        System.out.println("修改用户失败！");
                    }
                    break;
                case 4:
                    Enumeration<AbstractUser> users=DataProcessing.listUser();
                    while(users.hasMoreElements()) {   //判断是否到最后一个元素
                        AbstractUser user=users.nextElement();
                        System.out.printf("%s\t%s\t%s\t\n",user.getName(),user.getPassword(),user.getRole());
                    }
                    break;
                case 5:
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
                case 6:
                    showFileList();
                    break;
                case 7:
                    System.out.println("请输入你要修改的密码：");
                    password = scan.next();
                    changeSelfInfo(password);
                    break;
                case 8:
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
