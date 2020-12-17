package util;

import dao.IManageDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
import vo.Resource;
import vo.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class ManageFilter implements Filter {

    static Connection con;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session =request.getSession();
        User user = (User) request.getSession().getAttribute("user");
        con = new DatabaseConnection().getConnection();
        IManageDao dao = DAOFactory.getManageDaoInstance(con);
        List<Resource> resourceList = new ArrayList<>();
        try {
            resourceList = dao.getResourceList(user);
            int flag=0;
            String url = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
            for (int i=0;i<resourceList.size();i++) {
                if (resourceList.get(i).getUrl().equals(url)) {
                    flag=1;
                }
            }
            if (flag==0) {
                session.setAttribute("msg", "抱歉，当前用户没有访问该资源的权限");
                session.setAttribute("jump","main.jsp");
                request.getRequestDispatcher("error.jsp").forward(servletRequest, servletResponse);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
