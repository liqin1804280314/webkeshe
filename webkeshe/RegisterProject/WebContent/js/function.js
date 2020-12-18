var moblie_info=false;
var code_info=false;
var pwd_info=false;
var repwd_info=false;
$(function(){
    //短信验证码倒计时
    var countdownHandler = function(){
        var $button = $(".sendVerifyCode");
        var number = 60;
        var countdown = function(){
            if (number == 0) {
                $button.attr("disabled",false);
                $button.html("发送验证码");
                number = 60;
                return;
            } else {
                $button.attr("disabled",true);
                $button.html(number + "秒重新发送");
                number--;
            }
            setTimeout(countdown,1000);
        }
        setTimeout(countdown,1000);
    }
    $("#tele").on("blur",function(){
    	var $mobile = $("input[name=mobile]");
        var data = {};
        data.mobile = $.trim($mobile.val());
        if(data.mobile ==''){
           $("#err1").text("请输入手机号码").css("color","red");
            return;
        }
        var reg = /^1\d{10}$/;
        if(!reg.test(data.mobile)){
        	 $("#err1").text("请输入合法的手机号码").css("color","red");
            return ;
        }
        else{
        	 $("#err1").text("√").css("color","green");
        	 moblie_info=true;
        }
    });
    $("#tele").on("focus",function(){
    	$("#err1").text("");
    });
    $("#code").on("blur",function(){
    	var $code = $("input[name=verifyCode]");
        var data = {};
        data.code = $.trim($code.val());
        if(data.code ==''){
           $("#err2").text("请输入验证码码").css("color","red");
            return;
        }
        else{
        	 $("#err2").text("√").css("color","green");
        	 code_info=true;
        }
    });
    $("#code").on("focus",function(){
    	$("#err2").text("");
    });
    $("#pwd").on("blur",function(){
    	var $pwd = $("input[name=pwd]");
        var data = {};
        data.pwd = $.trim($pwd.val());
        if(data.pwd ==''){
           $("#err3").text("请输入密码").css("color","red");
            return;
        }
        if(data.pwd.length<8||data.pwd.length>20){
        	 $("#err3").text("请输入8-20位字符").css("color","red");
            return ;
        }
        else{
        	 $("#err3").text("√").css("color","green");
        	 pwd_info=true;
        }
    });
    $("#pwd").on("focus",function(){
    	$("#err3").text("");
    });
    $("#repwd").on("blur",function(){
    	var $repwd = $("input[name=repwd]");
        var data = {};
        data.repwd = $.trim($repwd.val());
        if(data.repwd ==''){
           $("#err4").text("请输入确认密码").css("color","red");
            return;
        }
        if(data.repwd!=$("input[name=pwd]").val()){
        	 $("#err4").text("两次密码不一致").css("color","red");
            return ;
        }
        else{
        	 $("#err4").text("√").css("color","green");
        	 repwd_info=true;
        }
    });
    $("#repwd").on("focus",function(){
    	$("#err4").text("");
    });
    //发送短信验证码
    $(".sendVerifyCode").on("click", function(){
        var $mobile = $("input[name=mobile]");
        var data = {};
        data.mobile = $.trim($mobile.val());
        if(data.mobile ==''){
            alert('请输入手机号码');
            return;
        }
        var reg = /^1\d{10}$/;
        if(!reg.test(data.mobile)){
            alert('请输入合法的手机号码');
            return ;
        }
        $.ajax({
            url:"sendSms.do",
            //async : true,
            type: "post",
            dataType: "json",
            data: {mobile:data.mobile},
            success: function (response) {
                if(response.answer == 'success'){
                    countdownHandler();
                    return ;
                }
                alert(response.answer);
            }
        });
    })
    //提交
    $(".sub-btn").on("click", function(){
        var data = {};
        data.mobile = $.trim($("input[name=mobile]").val());
        data.verifyCode = $.trim($("input[name=verifyCode]").val());
        data.pwd=$.trim($("input[name=pwd]").val());
        data.repwd=$.trim($("input[name=repwd]").val());
        if(data.mobile == ''){
            alert("请输入手机号");
            return ;
        }
        if(data.verifyCode == ''){
            alert("请输入验证码");
            return ;
        }
        if(data.pwd == ''){
            alert("请输入密码");
            return ;
        }
        if(data.repwd == ''){
            alert("请输入确认密码");
            return ;
        }
        if(!$("#check").get(0).checked){
        	 alert("请同意阅读协议");
             return ;
        }
        $.ajax({
            url:"register.do",
           // async : true,
            type: "post",
            dataType: "json",
            data: {mobile:data.mobile,verifyCode: data.verifyCode,pwd:data.pwd,repwd:data.repwd},
            success: function (response) {
                if(response.answer == 'success'){
                    alert("注册成功");
                    return ;
                }
                alert(response.answer);
            }
        });
    })
});