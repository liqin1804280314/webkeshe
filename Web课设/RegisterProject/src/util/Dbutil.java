package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dbutil {
	static String url="jdbc:mysql://localhost:3306/reg?useUnicode=true&charaterEncoding=UTF8&serverTimezone=GMT%2B8";

	static String user="root";//数据库对应的用户名，一般默认情况下都是root
	static String password="123";//数据库的密码
	static String driver="com.mysql.cj.jdbc.Driver";//注意确定和数据库的版本
	//数据库所需要的驱动类	
	//connection是java中连接数据库的类，里面包含了连接数据库的信息
	public static Connection getCon() {//连接数据的方法
		//1.先装驱动（固定格式）
		try {
			Class.forName(driver);
			//2.获取数据库的连接（固定格式）
			//使用驱动管理来获取数据库的连接
			Connection con=DriverManager.getConnection(url,user,password);
			//数据库的地址，对应的用户名和密码,得到对应的数据库连接
			return con;//将数据库的连接返回
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void closeAll(Connection con,PreparedStatement psmt,ResultSet rs) {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(psmt!=null) {
			try {
				psmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
