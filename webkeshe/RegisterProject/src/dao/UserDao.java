package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.User;
import util.Dbutil;

public class UserDao {
	public static void insertUser(String tele,String pwd) {
		Connection con=Dbutil.getCon();
		PreparedStatement psmt=null;
		ResultSet rs=null;
		String sql="insert into user (tele,pwd) value(?,?)";
		try {
			psmt=con.prepareStatement(sql);
			psmt.setString(1,tele);
			psmt.setString(2,pwd);
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			Dbutil.closeAll(con, psmt, rs);
		}
	}
}
