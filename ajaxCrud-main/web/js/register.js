var xmlHttp;
function createXmlHttp(){
    if(window.XMLHttpRequest){
        xmlHttp = new XMLHttpRequest();
    }
    else{
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function checkNull(e) {
    if ((e.id=="province"&&e.value=="")||(e.id=="city"&&e.value=="")){
        document.getElementById(e.id+'Error').innerText='必须选择'+e.alt
    }
    else if (e.id=="province"||(e.id=="city")){
        document.getElementById(e.id+'Error').style.color="green";
        document.getElementById(e.id+'Error').innerText="符合要求"
    }
    else if (e.value.match(/^[ ]*$/)) {
        document.getElementById(e.id+'Error').style.color="red";
        document.getElementById(e.id+'Error').innerText=e.alt+'不能为空';
    } else {
        var funcName = e.id+'Check';
        window[funcName]();
    }
}
function userNameCheck(){
    var username = document.getElementById('userName').value;
    if (!username.match(/^([a-zA-Z])([a-zA-Z]|[0-9]){3,14}$/)) {
        $("#userNameError").text('用户名只能使用英文字母和数字，以字母开头，长度为4到15个字符')
        $("#userNameError").css("color","red")
    }
    else{
        $.ajax(
            {
                type:"post",
                url:'RegisterCheck.do',
                data:{ userNameCheck:$("#userName").val()},
                dataType:"json",
                success: function (response) {
                    if (response.code==0){
                        $("#userNameError").text('用户名符合要求');
                        $("#userNameError").css("color","green")
                    }else{
                        $("#userNameError").text('用户名已存在');
                        $("#userNameError").css("color","red")
                    }
                }
            }
        )}
}
function trueNameCheck() {
    if (!$("#trueName").val().match(/^[\u4E00-\u9FA5]{2,4}$/)){
        $("#trueNameError").text("真实姓名只能是2-4长度的中文");
        $("#trueNameError").css("color","red");
    }
    else{
        $("#trueNameError").css("color","green")
        $("#trueNameError").text("真实姓名符合要求");
    }
}
function passwordCheck() {

    var password = $("#password").val();
    console.log(password)
    if (password.length<4){
        $('#passwordError').css("color","red")
        $('#passwordError').text("密码最小长度为4")
    }
    else{
        $('#passwordError').css("color","green")
        $('#passwordError').text('密码符合要求')
    }
}
function ensurePasswordCheck(){
    var ensurePassword = document.getElementById('ensurePassword').value;
    if (ensurePassword!=$('#password').val()||$('#ensurePassword').val().length<4){
        $('#ensurePasswordError').text('密码不一致或长度不够!');
        $('#ensurePasswordError').css("color","red")
    }
    else{
        $('#ensurePasswordError').text('密码符合要求')
        $('#ensurePasswordError').css("color","green")
    }
}
function emailCheck() {
    var email = document.getElementById('email').value;

    if (!email.match(/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/)){
        $('#emailError').text('邮箱地址格式不正确');
        $('#emailError').css("color","red");
    }
    else{
        $.ajax({
            type:"post",
            url: "RegisterCheck.do",
            data:{ emailCheck: $("#email").val()},
            dataType: "json",
            success: function (response) {
                if (response.code==0){
                    $("#emailError").text('邮箱名符合要求')
                    $('#emailError').css("color","green");
                }else{
                    $("#emailError").text('邮箱名已存在')
                    $('#emailError').css("color","red");
                }

            }
        })
    }
}

window.onload=function ()
{
    $.ajax({
        type:"post",
        url:"https://api.xiaohuwei.cn/test.php?type=province",
        data:{},
        dataType:"json",
        success:function (response) {
            var provinceElement = document.getElementById('province');
            provinceElement.options.length=0;
            provinceElement.add(new Option("请选择省",""));
            provinceElement.options[0].disabled=true;
            for (var index = 0;index<response.provincelist.length;index++){
                provinceElement.add(new Option(response.provincelist[index].province,response.provincelist[index].pid));
            }
        }
    })
    $("#province").change(function (e) {
        document.getElementById('city').disabled = false;
        $("#city").empty();
        $("#city").append($("<option>").val("").text("请选择城市"));
        var pid = $("#province").val();
        var city =  document.getElementById('province').options[this.selectedIndex].innerHTML
        document.getElementById('city').options[0].disabled=true;
        $.ajax({
            type:"get",
            data:{pid:pid},
            url : "https://api.xiaohuwei.cn/test.php?type=city",
            dataType:"json",
            success:function (response) {
                for (var index =0;index<response.citylist.length;index++){
                    var option = $("<option>").val(response.citylist[index].id).text(response.citylist[index].city);
                    $("#city").append(option);
                }
            }
        })
    })
}
function register() {
    var pElement = document.getElementsByTagName('p');
    var flag  = 0 ;
    for (var index=0;index<pElement.length;index++){
        if (pElement[index].innerText.indexOf('符合要求')!=-1){
            flag++;
        }
    }

    if (flag!=7){
        document.getElementById('registerError').innerText='请完善个人信息'
    }
    else{
        document.getElementById('registerError').innerText='';
        var par = $("#userInfo").serialize();
        console.log(par)
        var province = document.getElementById('province').options[document.getElementById('province').selectedIndex].innerHTML;
        var city = document.getElementById('city').options[document.getElementById('city').selectedIndex].innerHTML;
        $.ajax({
            type:"post",
            url:"Register.do?"+par,
            data:{"province":province,"city":city,"alt":"add"},
            dataType:"json",
            success:function (response) {
                if (response.code==0){
                 window.location.href='login.html';
                }
                else if (response.code==1){
                    alert(response.info);
                }
            }
        })
    }
}
jQuery.prototype.serializeObject=function(){
    var obj=new Object();
    $.each(this.serializeArray(),function(index,param){
        if(!(param.name in obj)){
            obj[param.name]=param.value;
        }
    });
    return obj;
}