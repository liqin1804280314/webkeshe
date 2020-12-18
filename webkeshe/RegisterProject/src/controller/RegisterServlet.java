package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
  
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import dao.UserDao;
  
/**
 * 注册
 */
@WebServlet("/register.do")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
     
    public RegisterServlet() {
        super();
    }
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Map<String,Object> map=new HashMap<String, Object>();
        String mobile = request.getParameter("mobile");
        String verifyCode = request.getParameter("verifyCode");
        String pwd=request.getParameter("pwd");
        System.out.println(mobile);
        System.out.println(verifyCode);
        JSONObject json = (JSONObject)request.getSession().getAttribute("verifyCode");
        if(json == null){
        	map.put("answer", "验证码错误");
            renderData(response, map);
            return ;
        }
        if(!json.getString("mobile").equals(mobile)){
        	map.put("answer", "电话号码错误");
            renderData(response, map);
            return ;
        }
        if(!json.getString("verifyCode").equals(verifyCode)){
        	map.put("answer",  "验证码错误");
            renderData(response, map);
            return ;
        }
        if((System.currentTimeMillis() - json.getLong("createTime")) > 1000 * 60 * 1){
        	map.put("answer",  "验证码已过期");
            renderData(response,map);
            return ;
        }
        //其他业务代码
        map.put("answer",  "success");
        UserDao.insertUser(mobile,pwd);
        renderData(response, map);
    }
    protected void renderData(HttpServletResponse response,Map<String,Object> map){
        try {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(new Gson().toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
