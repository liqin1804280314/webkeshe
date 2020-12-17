package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.IUserDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
import util.MD5Util;
import vo.Page;
import vo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@WebServlet(urlPatterns = "/userManage.do")
public class UserManage extends HttpServlet {
    static Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ok");
        HashMap<String,Object> map = new HashMap<String,Object>();
        String resetUser = req.getParameter("resetUser");
        String resetWay = req.getParameter("resetWay");
        List<User> userList = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();
        String[] name = resetUser.split(",");
        con = new DatabaseConnection().getConnection();
        IUserDao dao = DAOFactory.getUserDAOInstance(con);
        try {
            for (int i = 0; i < name.length; i++) {
                userNameList.add(name[i]);
            }
            if (resetWay.equals("userName")) {
                int flag=0;
                for (int i = 0; i < userNameList.size(); i++) {
                    User user = new User();
                    String password = userNameList.get(i).substring(0,4);
                    user.setPassword(MD5Util.md5(password));
                    user.setUsername(userNameList.get(i));
                    userList.add(user);
                }
                for (int i = 0; i < userList.size(); i++) {
                    if (!dao.updateUser(userList.get(i))) {
                        flag=1;
                    }
                }
                map.put("code",flag);
                String info = flag == 0 ? "密码已成功重置" : "密码重置失败";
                map.put("info", info);
            } else if (resetWay.equals("email")) {
                Page page = new Page(1,2,"","");
                List<User> list = new ArrayList<>();
                int flag=0;
                for(int i=0;i<userNameList.size();i++){
                    User user = new User();
                    user.setUsername(userNameList.get(i));
                    userList.add(user);
                }
                for (int i=0;i<userList.size();i++){
                    list =dao.query(userList.get(i),page);
                    User user =new User();
                    user.setPassword(MD5Util.md5(list.get(0).getEmail().substring(0,6)));
                    user.setUsername(userNameList.get(i));
                    if (!dao.updateUser(user)){
                        flag=1;
                    }
                }
                map.put("code",flag);
                String info = flag == 0 ? "密码已成功重置" : "密码重置失败";
                map.put("info", info);
            }
            String jsonStr = new Gson().toJson(map);
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(jsonStr);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParams = req.getParameter("pageParams");
        String queryParams = req.getParameter("queryParams");
        Gson gson = new GsonBuilder().serializeNulls().create();
        Page page = gson.fromJson(pageParams, vo.Page.class);
        User user = gson.fromJson(queryParams, User.class);

        con = new DatabaseConnection().getConnection();
        IUserDao dao = DAOFactory.getUserDAOInstance(con);
        try {
            List<User> userList = dao.query(user,page);
            int total = dao.count(user);
            HashMap<String,Object> mapReturn = new HashMap<String,Object>();
            mapReturn.put("total", total);
            mapReturn.put("rows", userList);
            String jsonStr = new Gson().toJson(mapReturn);
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(jsonStr);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        List<String> userNameList= new ArrayList<>();
        String[] name = userName.split(",");
        for (int i=0;i<name.length;i++){
            userNameList.add(name[i]);
        }
        System.out.println(userNameList);
        con = new DatabaseConnection().getConnection();
        IUserDao dao = DAOFactory.getUserDAOInstance(con);
        try {
            HashMap<String,Object> map = new HashMap<String,Object>();
            if (dao.deleteUser(userNameList)){
                map.put("info", "用户已成功删除");
            }else{
                map.put("info","用户在删除中遇到错误");
            }
            String jsonStr = new Gson().toJson(map);
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(jsonStr);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
