package util;

import com.google.gson.Gson;
import vo.User;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;


//拦截器，检测登录状态
public class PermissionFilter implements Filter {

    static Connection con;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化了");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
        if (url.equals("error.jsp")){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        User cookieUser = null;
        HttpSession session =request.getSession();
         Cookie[] cookies =  request.getCookies();
         for (Cookie c : cookies){
             String name = c.getName();
             if ("loginAlready".equals(name)){
                 String userInfo = c.getValue();
                 userInfo = URLDecoder.decode(userInfo,"utf-8");
                  cookieUser  = new Gson().fromJson(userInfo,User.class);
                 System.out.println(cookieUser.toString());
                 session.setAttribute("user",cookieUser);
             }
         }
        User user = (User) request.getSession().getAttribute("user");
        if (user==null&&cookieUser==null){
            //request.getRequestDispatcher("login.html").forward(servletRequest, servletResponse);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            session.setAttribute("msg","抱歉，您必须先登录才能访问该资源");
            session.setAttribute("jump","login.html");
            request.getRequestDispatcher("error.jsp").forward(request,response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁了");
    }
}
