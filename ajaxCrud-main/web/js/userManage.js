$(document).ready(function () {
    $("#btDelete").click(function () {
        deleteUser(this);
    })
    resetPassword();
    CloseDiv('updateDiv', 'fade');
    sortChange();
    jumpHelp();
    load();
    check();
    $("#next").click(function () {
        var pageNum = $("#pageNumber").text() * 1;
        var pageCount = $("#pageCount").text() * 1;
        if (pageNum < pageCount) {
            $("#pageNumber").text($("#pageNumber").text() * 1 + 1);
            getData();
        } else {
            alert("当前已为最后一页");
        }
    });
    $("#back").click(function () {
        var pageNum = $("#pageNumber").text() * 1;
        if (pageNum > 1) {
            $("#pageNumber").text($("#pageNumber").text() * 1 - 1);
            getData();
        } else {
            alert("当前已经是第一页");
        }
    });
    $("#first").click(function () {
        var pageNum = $("#pageNumber").text() * 1;
        if (pageNum != 1) {
            $("#pageNumber").text("1");
            getData()
        }
    });
    $("#btSearch").click(getData());
    $("#btClear").click(function () {
        $("#resetBtn").trigger('click');
    })
    $("#last").click(function () {
        var pageCount = $("#pageCount").text() * 1;
        var pageNum = $("#pageNumber").text() * 1;
        if (pageNum != pageCount) {
            $("#pageNumber").text(pageCount);
            getData()
        }
    })
    $("tbody").on("mouseover", "tr", function () {
        $(this).addClass('tr_hover'); //通过jQuery控制实现鼠标悬停上的背景色
    });
    $("tbody").on("mouseout", "tr", function () {
        $(this).removeClass('tr_hover'); //通过jQuery控制实现鼠标悬停上的背景色
    });
    $("tbody").on("click", "tr input:checkbox", function () {
        if (this.checked == true) {
            $(this).parents("tr").addClass('tr_select');
        } else {
            $(this).parents("tr").removeClass('tr_select');
        }
    });
    $("tbody").on("click", "tr td a#btnUpdate ", function () {
        distinct(this);
    });
    $("tbody").on("click", "tr td a#btnDel ", function () {
        deleteUser(this);
    })
    $("thead").on("click", "tr input:checkbox", function () {
        if (this.checked == true) {
            var input = document.getElementsByTagName("input");
            $("tbody input:checkbox").attr("checked", true);
            $("tbody").children("tr").addClass('tr_select');
        } else {
            $("tbody").children("tr").removeClass('tr_select');
            var input = document.getElementsByTagName("input");
            $("tbody input:checkbox").attr("checked", false);
        }
    });

    $("#pageSize").change(function () {
        getData();
    })
    mangeRight();
    getData();
    Add();
    disabledUser();
    CloseDiv('MyDiv', 'fade');
});

function getData() {
    var opt = document.getElementById('pageSize').options;
    var pageSize = opt[opt.selectedIndex].innerText;
    var pageNum = $("#pageNumber").text();
    var sort = "";
    var sortOrder = "";
    if ($("#usernameSort").attr("class") == 'glyphicon glyphicon-arrow-up' && $("#sortByUserName").attr("class") == 'sort_class') {
        sort = sort + "username";
        sortOrder = sortOrder + "asc";
    } else if ($("#usernameSort").attr("class") == 'glyphicon glyphicon-arrow-down' && $("#sortByUserName").attr("class") == 'sort_class') {
        sort = sort + "username";
        sortOrder = sortOrder + "desc";
    }
    if ($("#provinceSort").attr("class") == 'glyphicon glyphicon-arrow-up' && $("#sortByProvinceName").attr("class") == 'sort_class') {
        sort = sort + "province";
        sortOrder = sortOrder + "asc";
    } else if ($("#provinceSort").attr("class") == 'glyphicon glyphicon-arrow-down' && $("#sortByProvinceName").attr("class") == 'sort_class') {
        sort = sort + "province";
        sortOrder = sortOrder + "desc";
    }
    var queryParams = JSON.stringify($("#searchForm").serializeObject());
    var page = {pageSize: pageSize, pageNumber: pageNum, sort: sort, sortOrder: sortOrder};
    var pageParams = JSON.stringify(page);
    $.ajax({
        type: "post",
        data: {"queryParams": queryParams, "pageParams": pageParams},
        url: "http://localhost/userManage.do",
        dataType: "json",
        success: function (response) {
            var rows = response.rows;
            var total = response.total;
            var pageCount = Math.ceil(total / pageSize);
            $("#total").text(total);
            $("#pageCount").text(pageCount);
            $("tbody").empty();
            $.each(rows, function (index, row) {
                var bgColor
                if (index % 2 == 0)
                    bgColor = "#f6f4f0"
                else
                    bgColor = "#ffffff"
                var s = JSON.stringify(row);
                var str = "<tr data='" + s + "' bgcolor='" + bgColor + "'>";
                str = str + '<td><input type="checkbox" value=' + row.username + '/></td>';
                str = str + '<td>' + row.username + '</td>';
                str = str + '<td>' + row.chrName + '</td>';
                str = str + '<td>' + row.email + '</td>';
                str = str + '<td>' + row.provinceName + '</td>';
                str = str + '<td>' + row.city + '</td>';
                str = str + '<td><a href="#" id="btnDel" value=' + row.username + '>删除 </a>';
                str = str + '<a href="#" id="btnUpdate">&nbsp;&nbsp; 修改</a></td>';
                str = str + '</tr>';
                $("tbody").append(str);
            });
            if (pageNum == pageCount) {
                $("#next").css("color", "grey");
            } else {
                $("#next").css("color", "#337ab7");
            }
            if (pageNum == "1") {
                $("#back").css("color", "grey");
            } else {
                $("#back").css("color", "#337ab7");
            }

        }
    });
}

function sortChange() {
    $("#usernameSort,#provinceSort").click(function () {
        if ($(this).attr("class") == 'glyphicon glyphicon-sort') {

            $(this).removeClass("glyphicon glyphicon-sort");
            $(this).addClass("glyphicon glyphicon-arrow-up");
            $(this).parents("th").css("background", "#2b7cd3");
            $(this).parents("th").addClass("sort_class");
            if ($(this).attr("id") == 'usernameSort') {
                $(this).parents("th").next().next().next().css("background", "#0099cc");
                $(this).parents("th").next().next().next().removeClass("sort_class");
            }
            if ($(this).attr("id") == 'provinceSort') {
                $(this).parents("th").prev().prev().prev().removeClass("sort_class");
                $(this).parents("th").prev().prev().prev().css("background", "#0099cc");
            }
            $(this).parents("th").css("background", "#1c9b5d");
        } else if ($(this).attr("class") == 'glyphicon glyphicon-arrow-up') {

            $(this).removeClass("glyphicon glyphicon-arrow-up");
            $(this).addClass("glyphicon glyphicon-arrow-down");
            $(this).parents("th").addClass("sort_class");
            if ($(this).attr("id") == 'usernameSort') {

                $(this).parents("th").next().next().next().css("background", "#0099cc");
                $(this).parents("th").next().next().next().removeClass("sort_class");
            }
            if ($(this).attr("id") == 'provinceSort') {

                $(this).parents("th").prev().prev().prev().removeClass("sort_class");
                $(this).parents("th").prev().prev().prev().css("background", "#0099cc");
            }
            $(this).parents("th").css("background", "#1c9b5d");
        } else if ($(this).attr("class") == 'glyphicon glyphicon-arrow-down') {
            $(this).removeClass("glyphicon glyphicon-arrow-down");
            $(this).addClass("glyphicon glyphicon-sort");
            $(this).parents("th").removeClass("sort_class");
            if ($(this).attr("id") == 'usernameSort') {
                $(this).parents("th").next().next().next().css("background", "#0099cc");
                $(this).parents("th").next().next().next().removeClass("sort_class");
            }
            if ($(this).attr("id") == 'provinceSort') {
                $(this).parents("th").prev().prev().prev().removeClass("sort_class");
                $(this).parents("th").prev().prev().prev().css("background", "#0099cc");
            }
            $(this).parents("th").css("background", "#0099cc");
        }
        getData();
    })
}

function showDiv(show_div, bg_div) {
    $("#" + show_div).css("display", "block");
    $("#" + bg_div).css("display", "block");
    var windowHeight = $(window).height();
    var windowWidth = $(window).width();
    var popupHeight = $("#" + show_div).height();
    var popupWeight = $("#" + show_div).width();
    var posTop = (windowHeight - popupHeight) / 2;
    var posLeft = (windowWidth - popupWeight) / 2;
    $("#" + show_div).css({
        "left": posLeft + "px",
        "position": "absolute",
        "top": posTop + "px",
        "display": "block"
    });
}

function CloseDiv(show_div, bg_div) {
    $("#" + show_div).css("display", "none");
    $("#" + bg_div).css("display", "none");
}

jQuery.prototype.serializeObject = function () {
    var obj = new Object();
    $.each(this.serializeArray(), function (index, param) {
        if (!(param.name in obj)) {
            obj[param.name] = param.value;
        }
    });
    return obj;
}

function check() {
    $("#userName,#trueName,#password,#ensurePassword,#email,#province,#city").blur(function () {
        checkNull(this);
    });
}

function checkNull(e) {
    if ((e.id == "province" && e.value == "") || (e.id == "city" && e.value == "")) {
        document.getElementById(e.id + 'Error').innerText = '必须选择省市'
    } else if (e.id == "province" || (e.id == "city")) {
        document.getElementById(e.id + 'Error').style.color = "green";
        document.getElementById(e.id + 'Error').innerText = "符合要求"
    } else if (e.value.match(/^[ ]*$/)) {
        document.getElementById(e.id + 'Error').style.color = "red";
        document.getElementById(e.id + 'Error').innerText = e.alt + '不能为空';
    } else {
        var funcName = e.id + 'Check';
        if (window[funcName]() == "1") {
            document.getElementById(e.id + 'Error').style.color = "green";
            document.getElementById(e.id + 'Error').innerText = e.alt + "符合要求"
        }
    }
}

function userNameCheck() {
    var username = document.getElementById('userName').value;
    if (!username.match(/^([a-zA-Z])([a-zA-Z]|[0-9]){3,14}$/)) {
        $("#userNameError").text('用户名只能使用英文字母和数字，以字母开头，长度为4到15个字符')
        $("#userNameError").css("color", "red")
    } else {
        $.ajax(
            {
                type: "post",
                url: 'RegisterCheck.do',
                data: {userNameCheck: $("#userName").val()},
                dataType: "json",
                success: function (response) {
                    if (response.code == 0) {
                        $("#userNameError").text('用户名符合要求');
                        $("#userNameError").css("color", "green")
                    } else {
                        $("#userNameError").text('用户名已存在');
                        $("#userNameError").css("color", "red")
                    }
                }
            }
        )
    }
}

function trueNameCheck() {
    if (!$("#trueName").val().match(/^[\u4E00-\u9FA5]{2,4}$/)) {
        $("#trueNameError").text("真实姓名只能是2-4长度的中文");
        $("#trueNameError").css("color", "red");
    } else {
        $("#trueNameError").css("color", "green")
        $("#trueNameError").text("真实姓名符合要求");
    }
}

function passwordCheck() {

    var password = $("#password").val();
    if (password.length < 4) {
        $('#passwordError').css("color", "red")
        $('#passwordError').text("密码最小长度为4")
    } else {
        $('#passwordError').css("color", "green")
        $('#passwordError').text('密码符合要求')
    }
}

function ensurePasswordCheck() {
    var ensurePassword = document.getElementById('ensurePassword').value;
    if (ensurePassword != $('#password').val() || $('#ensurePassword').val().length < 4) {
        $('#ensurePasswordError').text('密码不一致或长度不够!');
        $('#ensurePasswordError').css("color", "red")
    } else {
        $('#ensurePasswordError').text('密码符合要求')
        $('#ensurePasswordError').css("color", "green")
    }
}

function emailCheck() {
    var email = document.getElementById('email').value;

    if (!email.match(/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/)) {
        $('#emailError').text('邮箱地址格式不正确');
        $('#emailError').css("color", "red");
    } else {
        $.ajax({
            type: "post",
            url: "RegisterCheck.do",
            data: {emailCheck: $("#email").val()},
            dataType: "json",
            success: function (response) {
                if (response.code == 0) {
                    $("#emailError").text('邮箱名符合要求')
                    $('#emailError').css("color", "green");
                } else {
                    $("#emailError").text('邮箱名已存在')
                    $('#emailError').css("color", "red");
                }

            }
        })
    }
}

function load() {
    $.ajax({
        type: "post",
        url: "https://api.xiaohuwei.cn/test.php?type=province",
        data: {},
        dataType: "json",
        success: function (response) {
            var provinceElement = document.getElementById('province');
            provinceElement.options.length = 0;
            provinceElement.add(new Option("请选择省", ""));
            provinceElement.options[0].disabled = true;
            for (var index = 0; index < response.provincelist.length; index++) {
                provinceElement.add(new Option(response.provincelist[index].province, response.provincelist[index].pid));
            }
        }
    })
    $("#province").change(function (e) {
        document.getElementById('city').disabled = false;
        $("#city").empty();
        $("#city").append($("<option>").val("").text("请选择城市"));
        var pid = $("#province").val();
        var city = document.getElementById('province').options[this.selectedIndex].innerHTML
        document.getElementById('city').options[0].disabled = true;
        $.ajax({
            type: "get",
            data: {pid: pid},
            url: "https://api.xiaohuwei.cn/test.php?type=city",
            dataType: "json",
            success: function (response) {
                for (var index = 0; index < response.citylist.length; index++) {
                    var option = $("<option>").val(response.citylist[index].id).text(response.citylist[index].city);
                    $("#city").append(option);
                }
            }
        })

    })
}

function city(cityName) {
    document.getElementById('city').disabled = false;
    $("#city").empty();
    $("#city").append($("<option>").val("").text("请选择城市"));
    var pid = $("#province").val();
    document.getElementById('city').options[0].disabled = true;
    $.ajax({
        type: "get",
        data: {pid: pid},
        url: "https://api.xiaohuwei.cn/test.php?type=city",
        dataType: "json",
        success: function (response) {
            for (var index = 0; index < response.citylist.length; index++) {
                var option = $("<option>").val(response.citylist[index].id).text(response.citylist[index].city);
                $("#city").append(option);
            }
            var str = "option:contains(" + cityName + ")";
            $("#city").find(str).attr("selected", "selected");
        }
    })

}

function actionSubmit() {
    var pElement = document.getElementsByTagName('p');
    var flag = 0;
    for (var index = 0; index < pElement.length; index++) {
        if (pElement[index].innerText.indexOf('符合要求') != -1) {
            flag++;
        }
    }
    var alt = $("#userInfo").attr("alt") + "";

    if (flag != 7 && alt == 'add') {
        document.getElementById('registerError').innerText = '请完善个人信息'
    } else {
        document.getElementById('registerError').innerText = '';
        var par = $("#userInfo").serialize();
        var province = document.getElementById('province').options[document.getElementById('province').selectedIndex].innerHTML;
        var city = document.getElementById('city').options[document.getElementById('city').selectedIndex].innerHTML;
        $.ajax({
            type: "post",
            url: "Register.do?" + par,
            data: {"province": province, "city": city, "alt": alt},
            dataType: "json",
            success: function (response) {
                alert(response.info);
                CloseDiv('MyDiv', 'fade');
                getData();
            }
        })
    }
}

function jumpHelp() {
    $("#btUpdate").click(function () {
        showDiv('updateDiv', 'fade');
    })
}

function distinct(e) {
    $("#userInfo").attr("alt", "update");
    var selectElement = $(".tr_select");
    if (e.id == 'btnUpdate') {
        if (selectElement.length > 1) {
            alert("若想修改此用户请先取消选中其他用户");
            return;
        } else if ($(e).parents("tr").attr("class").indexOf('tr_select') == -1) {
            alert("请选择你需要修改的下拉框");
            return;
        } else if (selectElement.length == 0) {
            alert("请先勾选下拉框");
            return;
        }
    }
    if (selectElement.length == 1) {
        $("#userName").off("blur");
        $("#trueName").off("blur")
        $("#email").off("blur");
        $("#userName").attr("readonly", "true");
        $("#trueName").attr("readonly", "true");
        $("#email").attr("readonly", "true");
        $("#userNameError,#ensurePasswordError,#emailError,#trueNameError,#provinceError,#cityError,#checkError,#passwordError").text("");
        showDiv('MyDiv', 'fade');
        var user = JSON.parse(selectElement.attr("data"));
        $("#userName").val(user.username);
        $("#trueName").val(user.chrName);
        $("#email").val(user.email);
        $("#password").attr("placeholder", "修改密码")
        $("#ensurePassword").attr("placeholder", "确认密码");
        var str1 = "option:contains(" + user.provinceName + ")";
        $("#province").find(str1).attr("selected", "selected");
        city(user.city);
    } else if (selectElement.length == 0) {
        alert("请选择要修改的对象");
    } else {
        showDiv('updateDiv', 'fade');
    }

}

function Add() {
    $("#btAdd").click(function () {
        $("#userNameError,#ensurePasswordError,#emailError,#trueNameError,#provinceError,#cityError,#checkError,#passwordError").text("");
        $("#userInfo").attr("alt", "add");
        $("#userName").removeAttr("readonly")
        $("#trueName").removeAttr("readonly");
        $("#email").removeAttr("readonly");
        showDiv('MyDiv', 'fade');
        $("#userName,#trueName,#password,#ensurePassword,#email,#province,#city").on(check());
        $("#userName,#trueName,#password,#ensurePassword,#email").val("");
        $("#province").find("option:contains('请选择省')").attr("selected", "selected");
        $("#city").find("option:contains('请选择城市')").attr("disabled", "false");
        $("#city").find("option:contains('请选择城市')").attr("selected", "selected");
    })
}

function deleteUser(e) {
    var selectElement = $(".tr_select");
    console.log(e)
    if (e.id == 'btnDel') {
        if (selectElement.length > 1) {
            alert("若想删除此用户请先取消选中其他用户");
            return;
        } else if ($(e).parents("tr").attr("class").indexOf('tr_select') == -1) {
            alert("请选择你需要删除的下拉框");
            return;
        } else if (selectElement.length == 0) {
            alert("请先勾选下拉框");
            return;
        }
    }
    console.log("ok")
    var str = "";
    $(".tr_select").each(function () {
        var user = JSON.parse($(this).attr("data"));
        str = str + user.username + ",";
    })
    $.ajax({
        type: "delete",
        url: "/userManage.do?userName=" + str,
        dataType: "json",
        success: function (response) {
            console.log(response)
            alert(response.info);
            getData();
        }
    })
}

function resetPassword() {
    $("#resetPassword").click(function () {
        var checkVal = $('input:radio[name="resetWay"]:checked').val() + "";
        if (checkVal == "undefined") {
            alert("请先勾选重置密码的方式");
            return;
        }
        var str = "";
        $(".tr_select").each(function () {
            var user = JSON.parse($(this).attr("data"));
            str = str + user.username + ",";
        })
        $.ajax({
            type: "get",
            url: "userManage.do?resetUser=" + str + "&resetWay=" + checkVal,
            dataType: "json",
            success: function (response) {
                console.log(response);
                alert(response.info);
            }
        })
    })
}

function mangeRight() {
    $("#manageRight").click(function () {
        var checkVal = $('input:radio[name="right"]:checked').val() + "";
        if (checkVal == "undefined") {
            alert("请先勾选分配权限的类型");
            return;
        }
        var str = "";
        $(".tr_select").each(function () {
            var user = JSON.parse($(this).attr("data"));
            str = str + user.username + ",";
        });
        $.ajax({
            type: "get",
            url: "updateUser.do?userNameList=" + str + "&role=" + checkVal,
            dataType: "json",
            success: function (response) {
                alert(response.info);
            }
        })
    })
}

function disabledUser() {
    $("#disabledUser").click(function () {
        var str = "";
        $(".tr_select").each(function () {
            var user = JSON.parse($(this).attr("data"));
            str = str + user.username + ",";
        });
        $.ajax({
            type: "post",
            url: "updateUser.do",
            data: {"userNameStr": str},
            dataType: "json",
            success: function (response) {
                alert(response.info);
            }
        })
    })
}