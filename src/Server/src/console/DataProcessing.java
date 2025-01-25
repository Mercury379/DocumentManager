package console;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * TODO 数据处理类
 *
 * @author gongjing
 * @date 2016/10/13
 */
public class DataProcessing {

    static Hashtable<String, AbstractUser> users;
    static Hashtable<String, Doc> docs;
    private static boolean connectToDB = false;
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    static String uploadpath="D:\\面向对象与多线程\\uploadfile\\";
    static String downloadpath="D:\\面向对象与多线程\\downloadfile\\";

    public static boolean connectToDatabase(){
        String driverName="com.mysql.jdbc.Driver"; // 加载数据库驱动类
        String url="jdbc:mysql://localhost:3306/document?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL
        String user="root"; // 数据库用户
        String password="HEQuan20031108";

        try{
            Class.forName(driverName);
            connection=DriverManager.getConnection(url,user,password);
            connectToDB=true;
        }catch (ClassNotFoundException e){
            System.out.println("数据库驱动类名错误！");
        }catch (SQLException e){
            System.out.println("数据库连接错误！");
        }
        return connectToDB;
    }

    static {
        users = new Hashtable<String, AbstractUser>();
//        users.put("jack", new Operator("jack", "123", "operator"));
//        users.put("rose", new Browser("rose", "123", "browser"));
//        users.put("kate", new Administrator("kate", "123", "administrator"));
        init();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        docs = new Hashtable<String, Doc>();
//        docs.put("0001", new Doc("0001", "jack", timestamp, "Doc Source Java", "Doc.java"));
        File docFile=new File("doc.txt");
        if(!docFile.exists()){
            docs.put("0001", new Doc("0001", "jack", timestamp, "Doc Source Java", "Doc.java"));
        }else{
            try {
                ObjectInputStream in=new ObjectInputStream(new FileInputStream(docFile));
                docs=(Hashtable<String, Doc>) in.readObject();
                in.close();
            } catch (IOException e) {
                System.out.println("错误类型："+e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("错误类型："+e.getMessage());
            }
        }
    }

    /**
     * TODO 初始化，连接数据库
     *
     * @param
     * @return void
     * @throws
     */
    public static void init() {
        connectToDB = true;
        File userFile=new File("user.txt");
        if(!userFile.exists()){
            users.put("jack", new Operator("jack", "123", "operator"));
            users.put("rose", new Browser("rose", "123", "browser"));
            users.put("kate", new Administrator("kate", "123", "administrator"));
            return;
        }
        try {
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(userFile));
            users=(Hashtable<String, AbstractUser>) in.readObject();
            in.close();
        } catch (IOException e) {
            System.out.println("错误类型："+e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("错误类型："+e.getMessage());
        }
    }

    public static void beforeExit(){
        File userFile=new File("user.txt");
        File docFile=new File("doc.txt");
        try {
            ObjectOutputStream out1=new ObjectOutputStream(new FileOutputStream(userFile));
            out1.writeObject(users);
            out1.close();
        } catch (IOException e) {
            System.out.println("错误类型："+e.getMessage());
        }
        try {
            ObjectOutputStream out2=new ObjectOutputStream(new FileOutputStream(docFile));
            out2.writeObject(docs);
            out2.close();
        } catch (IOException e) {
            System.out.println("错误类型："+e.getMessage());
        }
    }

    /**
     * TODO 按档案编号搜索档案信息，返回null时表明未找到
     *
     * @param id
     * @return Doc
     * @throws SQLException
     */
    public static Doc searchDoc(String id) throws SQLException {
        if (!connectToDB) {
            System.out.println("数据库连接错误！");
            return null;
        }else{
            statement=connection.createStatement();
            String sql="select * from doc_info";
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()) {
                String Id = resultSet.getString("Id");
                if (Id.equals(id)) {
                    String creator = resultSet.getString("creator");
                    Timestamp timestamp = resultSet.getTimestamp("timestamp");
                    String description = resultSet.getString("description");
                    String filename = resultSet.getString("filename");
                    Doc temp = new Doc(id, creator, timestamp, description, filename);
                    return temp;
                }
            }
            return null;
        }
    }

    public static void fileOperation(String fileName,String directory,boolean ifUpload) throws IOException {
        File inputFile;
        File outputFile;
        if(ifUpload==true){  //上传档案
            inputFile=new File(directory+'\\'+fileName);
            outputFile=new File(uploadpath+inputFile.getName());
        }else{  //下载档案
            inputFile=new File(uploadpath+fileName);
            outputFile=new File(downloadpath+fileName);
        }
        BufferedInputStream sourceFile=new BufferedInputStream(new FileInputStream(inputFile));
        BufferedOutputStream targetFile=new BufferedOutputStream(new FileOutputStream(outputFile));
        byte[] buffer=new byte[1024];
        while(true){
            int byteRead=sourceFile.read(buffer);
            if(byteRead==-1){
                break;
            }
            targetFile.write(buffer,0,byteRead);
        }
        sourceFile.close();
        targetFile.close();
    }

    /**
     * TODO 列出所有档案信息
     *
     * @param
     * @return Enumeration<Doc>
     * @throws SQLException
     */
    public static Enumeration<Doc> listDoc() throws SQLException {
        if (!connectToDB) {
            throw new SQLException("数据库未连接！");
        }else{
            Vector<Doc> users =new Vector<Doc>();
            Doc temp=null;
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql="select * from doc_info";
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                String id=resultSet.getString("Id");
                String creator=resultSet.getString("creator");
                Timestamp timestamp=resultSet.getTimestamp("timestamp");
                String description=resultSet.getString("description");
                String filename=resultSet.getString("filename");
                temp=new Doc(id,creator,timestamp,description,filename);
                users.addElement(temp);
            }
            Enumeration<Doc> e = users.elements();
            return e;
        }
    }

    /**
     * TODO 插入新的档案
     *
     * @param id
     * @param creator
     * @param timestamp
     * @param description
     * @param filename
     * @return boolean
     * @throws SQLException
     */
    public static boolean insertDoc(String id, String creator, Timestamp timestamp, String description, String filename) throws SQLException {
//        Doc doc;
        if(!connectToDB){
            throw new IllegalStateException("数据库未连接！");
        }else{
            String sql="INSERT INTO doc_info (creator,timestamp,description,filename) VALUES (?,?,?,?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,creator);
            preparedStatement.setTimestamp(2,timestamp);
            preparedStatement.setString(3,description);
            preparedStatement.setString(4,filename);
            int temp=preparedStatement.executeUpdate();
            preparedStatement.close();
            if(temp!=0)
                return true;
            else{
                return false;
            }
        }
//        if (!connectToDB) {
//            throw new SQLException("Not Connected to Database");
//        }
//
//        if (docs.containsKey(id))
//            return false;
//        else {
//            doc = new Doc(id, creator, timestamp, description, filename);
//            docs.put(id, doc);
//            return true;
//        }
    }

    /**
     * TODO 按用户名搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name 用户名
     * @return AbstractUser
     * @throws SQLException
     */
    public static AbstractUser searchUser(String name) throws SQLException {
        if (!connectToDB) {
            System.out.println("数据库连接错误！");
            return null;
        }else{
            statement=connection.createStatement();
            String sql="select * from user_info";
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()) {
                String username = resultSet.getString("username");
                if (username.equals(name)) {
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    AbstractUser temp;
                    if(role.equals("administrator")){
                        temp = new Administrator(name, password,role);
                    }else if(role.equals("operator")){
                        temp=new Operator(name,password,role);
                    }else{
                        temp=new Browser(name,password,role);
                    }
                    return temp;
                }
            }
            return null;
        }
    }

    /**
     * TODO 按用户名、密码搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name     用户名
     * @param password 密码
     * @return AbstractUser
     * @throws SQLException
     */
    public static AbstractUser searchUser(String name, String password) throws SQLException {
        if (!connectToDB) {
            System.out.println("数据库连接错误！");
            return null;
        }else{
            statement=connection.createStatement();
            String sql="select * from user_info";
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()) {
                String username = resultSet.getString("username");
                String userpassword=resultSet.getString("password");
                if (username.equals(name)&&userpassword.equals(password)) {
                    String role = resultSet.getString("role");
                    AbstractUser temp;
                    if(role.equals("administrator")){
                        temp = new Administrator(name, password,role);
                    }else if(role.equals("operator")){
                        temp=new Operator(name,password,role);
                    }else{
                        temp=new Browser(name,password,role);
                    }
                    return temp;
                }
            }
            return null;
        }
    }

    /**
     * TODO 取出所有的用户
     *
     * @param
     * @return Enumeration<AbstractUser>
     * @throws SQLException
     */
    public static Enumeration<AbstractUser> listUser() throws SQLException {
        if (!connectToDB) {
            throw new SQLException("数据库未连接！");
        }else{
            Vector<AbstractUser> users =new Vector<AbstractUser>();
            AbstractUser temp=null;
            statement=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql="select * from user_info";
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                String username=resultSet.getString("username");
                String password=resultSet.getString("password");
                String role=resultSet.getString("role");
                switch (ROLE_ENUM.valueOf(role.toLowerCase())){
                    case administrator:
                        temp=new Administrator(username,password,role);
                        break;
                    case operator:
                        temp=new Operator(username,password,role);
                        break;
                    default:
                        temp=new Browser(username,password,role);
                }
                users.addElement(temp);
            }
            Enumeration<AbstractUser> e = users.elements();
            return e;
        }
    }

    /**
     * TODO 修改用户信息
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return boolean
     * @throws SQLException
     */
    public static boolean updateUser(String name, String password, String role) throws SQLException {
        if(!connectToDB){
            throw new IllegalStateException("数据库未连接！");
        }else{
            String sql="UPDATE user_info SET password='"+password+"',role='"+role+"'WHERE username='"+name+"'";
            statement=connection.createStatement();
            if(statement.executeUpdate(sql)!=0){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * TODO 插入新用户
     *
     * @param name     用户名
     * @param password 密码
     * @param role     角色
     * @return boolean
     * @throws SQLException
     */
    public static boolean insertUser(String name, String password, String role) throws SQLException {
        if(!connectToDB){
            throw new IllegalStateException("数据库未连接！");
        }else{
            String sql="INSERT INTO user_info (username,password,role) VALUES (?,?,?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,role);
            int temp=preparedStatement.executeUpdate();
            preparedStatement.close();
            if(temp!=0)
                return true;
            else{
                return false;
            }
        }
//        AbstractUser user;
//        if (users.containsKey(name)) {
//            return false;
//        } else {
//            switch (ROLE_ENUM.valueOf(role.toLowerCase())) {
//                case administrator:
//                    user = new Administrator(name, password, role);
//                    break;
//                case operator:
//                    user = new Operator(name, password, role);
//                    break;
//                default:
//                    user = new Browser(name, password, role);
//            }
//            users.put(name, user);
//            return true;
//        }
    }
    /**
     * TODO 删除指定用户
     *
     * @param name 用户名
     * @return boolean
     * @throws SQLException
     */
    public static boolean deleteUser(String name) throws SQLException {
        if(!connectToDB){
            throw new IllegalStateException("数据库未连接！");
        }else{
            statement=connection.createStatement();
            String sql="delete from user_info where username='"+name+"'";
            if(statement.executeUpdate(sql)==0){
                return false;
            }else{
                return true;
            }
        }
//        if (users.containsKey(name)) {
//            users.remove(name);
//            return true;
//        } else {
//            return false;
//        }
    }

    /**
     * TODO 关闭数据库连接
     *
     * @param
     * @return void
     * @throws
     */
    public static void disconnectFromDataBase() {
        if (connectToDB) {
            // close Statement and Connection
            try {
                connection.close();
                System.out.println("断开与数据库得到连接！");
            } catch(SQLException sqlException){
                sqlException.printStackTrace();
            }finally {
                connectToDB = false;
            }
        }
    }

    static enum ROLE_ENUM {
        /**
         * administrator
         */
        administrator("administrator"),
        /**
         * operator
         */
        operator("operator"),
        /**
         * browser
         */
        browser("browser");

        private String role;

        ROLE_ENUM(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

}

