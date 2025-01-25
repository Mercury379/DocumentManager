package console;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * TODO 抽象用户类，为各用户子类提供模板
 *
 * @author gongjing
 * @date 2016/10/13
 */
public abstract class AbstractUser implements Serializable {
    static final double EXCEPTION_PROBABILITY = 0.9;
    private String name;
    private String password;
    private String role;

    @Override
    public String toString() {
        return "AbstractUser{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    AbstractUser(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }


    /**
     * TODO 修改用户自身信息
     *
     * @param password 口令
     * @return boolean 修改是否成功
     * @throws SQLException
     */
    public boolean changeSelfInfo(String password) throws SQLException {
        if (DataProcessing.updateUser(name, password, role)) {
            this.password = password;
            System.out.println("修改成功");
            return true;
        } else {
            return false;
        }
    }

    /**
     * TODO 下载档案文件
     *
     * @param id 文件id
     * @return boolean 下载是否成功
     * @throws IOException
     */
    public boolean downloadFile(String id) throws IOException, SQLException {
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_PROBABILITY) {
            throw new IOException("Error in accessing file");
        }
        boolean ifUpload=false;
        String directory="";
        Doc file=DataProcessing.searchDoc(id);
        String filename=file.getFilename();
        DataProcessing.fileOperation(filename,directory,ifUpload);
        return true;
    }

    /**
     * TODO 展示档案文件列表
     *
     * @param
     * @return void
     * @throws SQLException
     */

    public void showFileList() throws SQLException {
        double ranValue = Math.random();
        if (ranValue > EXCEPTION_PROBABILITY) {
            throw new SQLException("Error in accessing file DB");
        }
        Enumeration<Doc> docs=DataProcessing.listDoc();
        System.out.println("id\t\t文件名\t\t创建者\t\t描述\t\t\t创建时间");
        while(docs.hasMoreElements()) {   //判断是否到最后一个元素
            Doc doc=docs.nextElement();
            System.out.printf("%s\t%s\t%s\t%s\t%s\t\n",doc.getId(),doc.getFilename(),doc.getCreator(),doc.getDescription(),doc.getTimestamp());
        }
    }

    public boolean updateFile(String directory,String filname) throws IOException {
        boolean ifUpload=true;
        DataProcessing.fileOperation(filname,directory,ifUpload);
        return true;
    }

    /**
     * TODO 展示菜单，需子类加以覆盖
     *
     * @param
     * @return void
     * @throws
     */
    public abstract void showMenu() throws SQLException, IOException;

    /**
     * TODO 退出系统
     *
     * @param
     * @return void
     * @throws
     */
    public void exitSystem() {
        System.out.println("系统退出, 谢谢使用 ! ");
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
