var a = 11;
var b = setInterval(function(){
    var address = "<%="
    if(a > 0){
        a--;
        document.getElementById('showNum').innerHTML = a;
    }else{
        document.getElementById('showNum').innerHTML ='已自动帮你跳转';


        window.location.href=addressHref
    }
},1000);