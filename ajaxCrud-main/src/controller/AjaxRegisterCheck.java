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
import java.util.Map;

@WebServlet(urlPatterns = "/RegisterCheck.do")
public class AjaxRegisterCheck extends HttpServlet {
    static Connection con;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        int flag=0;
        if (req.getParameter("userNameCheck")!=null)flag=1;
        if (req.getParameter("emailCheck")!=null)flag=2;
        if (flag==1||flag==2){
            String userNameCheck = req.getParameter("userNameCheck");
            String emailCheck=req.getParameter("emailCheck");
            System.out.println(emailCheck);
            con=new DatabaseConnection().getConnection();
            IUserDao userDao = DAOFactory.getUserDAOInstance(con);
            User user1 = new User();
           switch (flag){
               case 1:user1.setUsername(userNameCheck);break;
               case 2:user1.setEmail(emailCheck);break;
           }
            try {
                if (userDao.selectOne(user1).getUsername()==null&&userDao.selectOne(user1).getEmail()==null){
                    map.put("code",0);
                }
                else{
                    map.put("code",1);
                }
                String jsonStr = new Gson().toJson(map);
                resp.setContentType("text/html;charset=UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(jsonStr);
                out.flush();
                out.close();
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
    }
}
