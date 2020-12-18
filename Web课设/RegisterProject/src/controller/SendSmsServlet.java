package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
  
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
  
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhenzi.sms.ZhenziSmsClient;
  
/**
 * 获取验证码
 */
@WebServlet("/sendSms.do")
public class SendSmsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //短信平台相关参数
    private String apiUrl = "https://sms_developer.zhenzikj.com";
    private String appId = "107471";
    private String appSecret = "376f8bc4-e000-4d81-bd39-f63c95920bcb";
        
    public SendSmsServlet() {
        super();
    }
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
  
    /**
     * 短信平台使用的是榛子云短信(smsow.zhenzikj.com)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       System.out.println("进入该servlet");
       request.setCharacterEncoding("utf-8");
       Map<String,Object> map=new HashMap<String, Object>();
    	try {
            String mobile = request.getParameter("mobile");
            System.out.println(mobile);
            JSONObject json = null;
            //生成6位验证码
            String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
            //发送短信
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
            Map<String, Object> params = new HashMap<String, Object>();
           params.put("number", mobile);
           params.put("templateId", "2726");
            String[] templateParams = new String[2];
            templateParams[0] = verifyCode;
            templateParams[1] = "1分钟";
            //发送短信
            params.put("templateParams", templateParams);
            String result =client.send(params);
            		//client.send(mobile, "您的验证码为:" + verifyCode + "，该码有效期为5分钟，该码只能使用一次!");
            System.out.println(result);
            json = JSONObject.parseObject(result);
            if(json.getIntValue("code") != 0){//发送短信失败
            	map.put("answer", "fail");
                renderData(response, map);
                return;
            }
            //将验证码存到session中,同时存入创建时间
            //以json存放，这里使用的是阿里的fastjson
            HttpSession session = request.getSession();
            json = new JSONObject();
            json.put("mobile", mobile);
            json.put("verifyCode", verifyCode);
            json.put("createTime", System.currentTimeMillis());
            // 将认证码存入SESSION
            request.getSession().setAttribute("verifyCode", json);
            map.put("answer", "success");
            renderData(response, map);
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    	 map.put("answer", "fail");
        renderData(response, map);
    }
     
    protected void renderData(HttpServletResponse response, Map<String,Object> map){
        try {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(new Gson().toJson(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}