package dbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {  //数据库连接函数
    private Connection con;
    //静态代码只会在第一次new时执行，以后再new一个对象时会执行
    private static String className;
    private static String url;
    private static String userName;
    private static String password;
    static {
        try{
            Properties pro = new Properties();
            InputStream is = DatabaseConnection.class.getResourceAsStream("/resource/jdbc.properties");
            pro.load(is);
            className=pro.getProperty("className");
            url = pro.getProperty("url");
            userName=pro.getProperty("userName");
            password=pro.getProperty("password");
            Class.forName(className);
            is.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public DatabaseConnection(){

    }
    public Connection getConnection(){
        try{
            con= DriverManager.getConnection(url,userName,password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public void close(){
        if (con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}