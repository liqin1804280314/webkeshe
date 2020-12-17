package controller;

import dao.IDownloadDao;
import dbc.DatabaseConnection;
import factory.DAOFactory;
import vo.Download;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

@WebServlet(urlPatterns = "/getDownloadList.do")
public class GetDownloadListController extends HttpServlet {
    static Connection con;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        con = new DatabaseConnection().getConnection();
        IDownloadDao downloadDao = DAOFactory.getDownloadDAOInstance(con);
        try {
            List<Download> downloadList = downloadDao.selectAll();
            System.out.println(downloadList);
            req.setCharacterEncoding("UTF-8");
            HttpSession session = req.getSession();
            session.setAttribute("dList",downloadList);
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.sendRedirect("download.jsp");
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
