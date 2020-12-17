package controller;

import com.google.gson.Gson;
import dao.IUserDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
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

@WebServlet(urlPatterns = "/updateUser.do")
public class ModifyController extends HttpServlet {
    static Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String,Object> map = new HashMap<String,Object>();
        String userName = req.getParameter("userNameList");
        String role = req.getParameter("role");
        String[] name = userName.split(",");
        con = new DatabaseConnection().getConnection();
        IUserDao dao = DAOFactory.getUserDAOInstance(con);
        try {
            int flag = 0;
            for (int i = 0; i < name.length; i++) {
                if (!dao.updateRole(name[i],role)) {
                    flag = 1;
                }
            }
            String info = flag == 0 ? "已成功分配好用户权限" : "分配用户权限失败";
            map.put("code", flag);
            map.put("info",info);
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
        HashMap<String,Object> map = new HashMap<String,Object>();
        String userNameStr = req.getParameter("userNameStr");
        String[] name = userNameStr.split(",");
        con = new DatabaseConnection().getConnection();
        IUserDao dao = DAOFactory.getUserDAOInstance(con);
        try {
            int flag = 0;
            for (int i = 0; i < name.length; i++) {
                if (!dao.updateRole(name[i], "禁用用户")) {
                    flag = 1;
                }
            }
            String info = flag==0?"已成功禁用用户":"禁用用户失败";
            map.put("code", flag);
            map.put("info", info);
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

}
