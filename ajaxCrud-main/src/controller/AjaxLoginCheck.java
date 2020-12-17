package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.IUserDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
import util.MD5Util;
import vo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/loginCheck.do")
public class AjaxLoginCheck extends HttpServlet {
    static Connection con;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        String userCode = req.getParameter("userCode");
        String checkBox = req.getParameter("checkBox");
        HttpSession session  = req.getSession();
        String verifyCode = (String)session.getAttribute("verifyCode");
        System.out.println(verifyCode);
        Map<String, Object> map = new HashMap<String, Object>();
        if(!userCode.equals(verifyCode)){
            map.put("code", 1);
            map.put("info", "验证码不正确");
        }else{
            con = new DatabaseConnection().getConnection();
            User user1 = new User();
            user1.setUsername(userName);
            user1.setPassword(MD5Util.md5(password));
            IUserDao dao  = DAOFactory.getUserDAOInstance(con);
            try {
                User user = dao.selectOne(user1);
                if (user.getUsername().equals("禁用")){
                    map.put("code",3);
                    map.put("info", "此用户已经被禁用");
                }
              else  if (user.getUsername()==null){
                    map.put("code", 2);
                    map.put("info", "用户名不正确");
                }else{
                    if(user.getPassword()==null){
                        map.put("code", 3);
                        map.put("info", "密码不正确");
                    }else{
                        session.setAttribute("user", user);
                        if (checkBox.equals("true")){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.setPrettyPrinting();
                            Gson gson = gsonBuilder.create();
                            String userInfo = gson.toJson(user);
                            userInfo = URLEncoder.encode(userInfo, "utf-8");
                            System.out.println(userInfo);
                            Cookie cookie = new Cookie("loginAlready", userInfo);
                            cookie.setMaxAge(60 * 60 * 24 * 7);
                            resp.addCookie(cookie);
                        }
                        map.put("code", 0);
                        map.put("info", "登陆成功");
                    }
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
