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

@WebServlet(urlPatterns = "/LoginController.do")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        con = new DatabaseConnection().getConnection();
        IUserDao userDao = DAOFactory.getUserDAOInstance(con);
        User user = new User();
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        String checkBox = request.getParameter("checkBox");

        user.setUsername(username);
        user.setPassword(MD5Util.md5(password));
        System.out.println("用户名：" + username + " 密码: " + password);
        HttpSession session = request.getSession();
        String vCode = (String) session.getAttribute("verifyCode");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        String verifyCode = request.getParameter("userCode");
        PrintWriter pw = response.getWriter();
        User user1 = null;
        try {
            user1 = userDao.selectOne(user);
            if (user1.getPassword() != null && verifyCode.equals(vCode)) {
                //response.sendRedirect("success.html");
                session.setAttribute("user", user1);
                if (checkBox!=null&&checkBox.equals("on")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setPrettyPrinting();
                    Gson gson = gsonBuilder.create();
                    String userInfo = gson.toJson(user1);
                    userInfo = URLEncoder.encode(userInfo, "utf-8");
                    System.out.println(userInfo);
                    Cookie cookie = new Cookie("loginAlready", userInfo);
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    response.addCookie(cookie);
                }
                response.sendRedirect("main.jsp");
            } else {
                String message = "";
                if (user1.getUsername() == null) {
                    message += "抱歉，您输入的用户名不存在";
                }
                if (user1.getUsername() != null && user1.getPassword() == null) {
                    message += "抱歉，您输入的密码不正确";
                }
                if (!verifyCode.equals(vCode)) {
                    message += "抱歉，您输入的验证码不正确";
                }
                session.setAttribute("msg", message);
                session.setAttribute("jump","login.html");
                request.getRequestDispatcher("error.jsp").forward(request, response);
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
    }
}
