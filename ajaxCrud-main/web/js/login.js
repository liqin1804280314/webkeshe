var xmlHttp;
function createXmlHttp(){
    if(window.XMLHttpRequest){
        xmlHttp = new XMLHttpRequest();
    }
    else{
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
}
function ajaxCheckLogin() {
    let userName = document.getElementById('userName').value;
    let password = document.getElementById('password').value;
    let userCode = document.getElementById('userCode').value;
    let checkBox = document.getElementById('checkBox').checked;
    var str;
    if(checkBox){
        str = "&checkBox=true"
    }
    else{
        str = "&checkBox=false";
    }
    createXmlHttp();
    xmlHttp.open("post", "loginCheck.do", true);
    xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    let data = "userName=" + userName + "&password=" + password + "&userCode=" + userCode+str;
    xmlHttp.send(data);
    xmlHttp.onreadystatechange= function () {
        if (xmlHttp.readyState==4&&xmlHttp.status==200){
            var response = xmlHttp.responseText;
            response = JSON.parse(response);
            if (response.code==0){
                window.location.href='main.jsp';
            }else{
                document.getElementById('checkError').innerText = response.info;
            }
        }
    }
}
function checkNull(e) {
    if (e.value.match(/^[ ]*$/)) {
        document.getElementById(e.id + 'Error').innerText = e.name + '不能为空';
    } else {
        document.getElementById(e.id + 'Error').innerText = '';
    }
}
function changeCode() {
    var codeImg = document.getElementById("verifyCode");
//这里URL后追加随机数，使程序每次访问的都是不同的资源，防止浏览器缓存，即产生不同的验证码
    codeImg.src = "servlet/CreateVerifyCodeImage.do?t=" + Math.random();
}
