package controller;

import com.google.gson.Gson;
import dao.IUserDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
import util.MD5Util;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/Register.do")
public class AjaxRegister extends HttpServlet {
    static Connection con;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        con = new DatabaseConnection().getConnection();
        IUserDao userDao = DAOFactory.getUserDAOInstance(con);
        String alt = req.getParameter("alt");
        User user = new User(req.getParameter("userName"), MD5Util.md5(req.getParameter("password")), req.getParameter("chrName"),
                req.getParameter("email"), req.getParameter("province"), req.getParameter("city"));
        if (alt!=null&&alt.equals("add")){
            try {
                if (userDao.insertOne(user)){
                    System.out.println("用户增加成功");
                    map.put("code",0);
                    map.put("info","用户增加成功");
                }else{
                    map.put("code",1);
                    map.put("info","增加用户过程中发生错误");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }else if (alt!=null&&alt.equals("update")){
            try {
                if (userDao.updateUser(user)){
                    System.out.println("用户信息修改成功");
                    map.put("code",0);
                    map.put("info","修改成功");
                }else{
                    map.put("code",1);
                    map.put("info","修改中发生错误");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    con.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        String jsonStr = new Gson().toJson(map);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(jsonStr);
        out.flush();
        out.close();

    }
}
