package controller;

import dao.CreateVerifyCodeImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(urlPatterns = "/servlet/CreateVerifyCodeImage.do")
public class CreateVerifyCodeImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateVerifyCodeImage createVerifyCodeImage = new CreateVerifyCodeImage();
        String vCode = createVerifyCodeImage.createCode();
        HttpSession session = req.getSession();
        session.setAttribute("verifyCode", vCode);
        resp.setContentType("img/jpeg");
        resp.setHeader("Pragma", "No-cache");
        resp.setDateHeader("Expires", 0);
        BufferedImage image = createVerifyCodeImage.CreateImage(vCode);
        ServletOutputStream out = resp.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        out.flush();
        out.close();
    }
}
