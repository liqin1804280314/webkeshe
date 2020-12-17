package dao.impl;


import dao.IUserDao;
import vo.Page;
import vo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IUserDaoImpl implements IUserDao {
    private Connection con;
    private PreparedStatement pst;
    public IUserDaoImpl(Connection con){
        this.con=con;
    }
    @Override
    public User selectOne(User user) throws Exception{
        User user1 =new User() ;
        String str = " select roleName from t_role where roleId = ( select roleId from t_user_role  where userName = ? )";
        this.pst = this.con.prepareStatement(str);
        this.pst.setString(1,user.getUsername());
        ResultSet rs1 = pst.executeQuery();
        if (rs1.next()){
            if (rs1.getString("roleName").equals("禁用用户")) {
                user1.setUsername("禁用");
                return user1;
            }
        }
        String sql = "select * from t_users where 1=1 ";
        if (user.getUsername()!=null){
            sql+="and username = ?";
            this.pst = this.con.prepareStatement(sql);
            this.pst.setString(1,user.getUsername());
        }
        if (user.getEmail()!=null&&user.getUsername()==null){
            sql+="and email = ?";
            this.pst = this.con.prepareStatement(sql);
            this.pst.setString(1,user.getEmail());
        }

        ResultSet rs = pst.executeQuery();
        if (rs.next()){
            String password = rs.getString("password");
            if (password.equals(user.getPassword())) {
                user1 = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("chrName"), rs.getString("email"), rs.getString("province"), rs.getString("city"));
            }
            else {
                user1.setUsername(user.getUsername());
                user1.setEmail(user.getEmail());
            }
        }
       return user1;
    }

    @Override
    public boolean insertOne(User user) throws Exception {
        String sql = "insert into t_users(userName,password,chrName,email,province,city) values(?,?,?,?,?,?) ";
        this.pst = this.con.prepareStatement(sql);
        this.pst.setString(1,user.getUsername());
        this.pst.setString(2, user.getPassword());
        this.pst.setString(3, user.getChrName());
        this.pst.setString(4, user.getEmail());
        this.pst.setString(5, user.getProvinceName());
        this.pst.setString(6,user.getCity());
        this.pst.executeUpdate();
        String sql1 = "insert into t_user_role(roleId,userName) values(2,?)";
        this.pst = this.con.prepareStatement(sql1);
        this.pst.setString(1, user.getUsername());
        int result = this.pst.executeUpdate();
        return result>0;
    }

    @Override
    public int count(User user) throws Exception {
        String sql = "select count(*) from t_users where 1=1 ";
        StringBuilder condition = new StringBuilder();
        if (!user.getUsername().equals("")){
            condition.append(" and username like '%").append(user.getUsername()).append("%'");
        }
        if (!user.getChrName().equals("")){
            condition.append(" and chrName like '%").append(user.getChrName()).append("%'");
        }
        if (!user.getEmail().equals("")){
            condition.append(" and email like '%").append(user.getEmail()).append("%'");
        }
        if (!user.getProvinceName().equals("")){
            condition.append(" and province like '%").append(user.getProvinceName()).append("%'");
        }
        sql=sql+condition;
        System.out.println(sql);
        this.pst = this.con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int count=0;
        if(rs.next()){
             count = rs.getInt(1);
        }
        return count;
    }


    @Override
    public boolean deleteUser(List<String> usernameList) throws Exception {
        String sql1 = " delete from t_user_role where userName=?";
        String sql2 = "delete from t_users where userName=?";

        int result=0;
        for (int i = 0; i < usernameList.size(); i++) {
            this.pst = this.con.prepareStatement(sql1);
            this.pst.setString(1, usernameList.get(i));
             result =  this.pst.executeUpdate();
             if (result==0){return false;}
            this.pst = this.con.prepareStatement(sql2);
            this.pst.setString(1, usernameList.get(i));
             result = this.pst.executeUpdate();
            if (result==0){return false;}
        }
        return true;
    }

    @Override
    public List<User> query(User user, Page page) throws Exception{
        List<User> userList = new ArrayList<>();
        StringBuilder condition = new StringBuilder();
        if (user.getUsername()!=null&&!user.getUsername().equals("")){
            condition.append(" and username like '%").append(user.getUsername()).append("%'");
        }
        if (user.getChrName()!=null&&!user.getChrName().equals("")){
            condition.append(" and chrName like '%").append(user.getChrName()).append("%'");
        }
        if (user.getEmail()!=null&&!user.getEmail().equals("")){
            condition.append(" and email like '%").append(user.getEmail()).append("%'");
        }
        if (user.getProvinceName()!=null&&!user.getProvinceName().equals("")){
            condition.append(" and province like '%").append(user.getProvinceName()).append("%'");
        }
       int begin = page.getPageSize()*(page.getPageNumber()-1);
        String sql = "select userName,chrName,email,province,city from t_users where 1=1 ";
        sql = sql + condition;
        if (!page.getSort().equals("")&&!page.getSortOrder().equals("")){
            sql = sql + "order by "+page.getSort()+" "+page.getSortOrder();
        }
        sql = sql +" limit " +begin +" , "+page.getPageSize();
        System.out.println(sql);
        this.pst = this.con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()){
            User user1 = new User(rs.getString("username"),rs.getString("chrName"),rs.getString("email"),
            rs.getString("province"),rs.getString("city"));
            userList.add(user1);
        }
        return userList;
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        String sql = "update t_users set userName= ?";
        StringBuilder condition = new StringBuilder();
        if (user.getProvinceName()!=null&&!user.getProvinceName().equals("")){
            condition.append(" , province = '").append(user.getProvinceName()).append("'");
        }
        if (user.getCity()!=null&&!user.getCity().equals("")){
            condition.append(" , city = '").append(user.getCity()).append("'");
        }
        if (user.getPassword()!=null&&!user.getPassword().equals("")){
            condition.append(" , password = '").append(user.getPassword()).append("'");
        }
        sql =sql + condition;
        sql = sql + " where userName = ?";
        System.out.println(sql);
        this.pst = this.con.prepareStatement(sql);
        this.pst.setString(1,user.getUsername());
        this.pst.setString(2,user.getUsername());
        int result = this.pst.executeUpdate();
        return result>0;
    }

    @Override
    public boolean updateRole(String userName,String role) throws Exception {
        String sql = "update t_user_role set roleId = (select roleId from t_role where roleName = ?) where userName = ?";
        this.pst = this.con.prepareStatement(sql);
        this.pst.setString(1,role);
        this.pst.setString(2,userName);
        int result = this.pst.executeUpdate();
        return result>0;
    }
}
