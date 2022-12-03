<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>云R记</title>
    <link href="lib/css/login.css" rel="stylesheet" type="text/css" />
    <script src="lib/js/jquery-1.11.3.js" type=text/javascript></script>
    <script src="lib/js/util.js" type=text/javascript></script>
    <script src="lib/js/config.js" type=text/javascript></script>


</head>
<body>
<!--head-->
<div id="head">
    <div class="top">
        <div class="fl yahei18">开启移动办公新时代！</div>
    </div>
</div>

<!--login box-->
<div class="wrapper">
    <div id="box">
        <div class="loginbar">用户登录</div>
        <div id="tabcon">
            <div class="box show">
                <form action="user" method="post" id="loginForm">
                 <input type="hidden" name="actionName" value="login"/>
                <input type="text" class="user yahei16" id="UserName" name="userName" value="" /><br /><br />
                <input type="password" class="pwd yahei16" id="UserPwd" value="" name="userPwd" /><br /><br />
                <input  type="checkbox" value="1"  class="inputcheckbox" name="rem"/> <label>记住我</label>&nbsp; &nbsp; <span id="Msg" >${resultInfo.msg}</span><br /><br />
                <input type="button" class="log jc yahei16" value="登 录" onclick="checkLogin()" />&nbsp; &nbsp;
                 <input type="reset" value="取 消" class="reg jc yahei18" />
                </form>
            </div>
        </div>
        </form>
    </div>
</div>

<div id="flash">
    <div class="pos">
        <a bgUrl="lib/images/banner-bg1.jpg" id="flash1" style="display:block;"><img src="lib/images/banner_pic1.png"></a>
        <a bgUrl="lib/images/banner-bg2.jpg" id="flash2"><img src="lib/images/banner-pic2.jpg"></a>
    </div>
    <div class="flash_bar">
        <div class="dq" id="f1" onclick="changeflash(1)"></div>
        <div class="no" id="f2" onclick="changeflash(2)"></div>
    </div>
</div>

<!--bottom-->
<div id="bottom">
    <div id="copyright">
        <div class="quick">
            <ul>
                <li><input type="button" class="quickbd iphone" onclick="location.href='http://lezijie.com'" /></li>
                <li><input type="button" class="quickbd android" onclick="location.href='http://lezijie.com'" /></li>
                <li><input type="button" class="quickbd pc" onclick="location.href='http://lezijie.com'" /></li>
                <div class="clr"></div>
            </ul>
            <div class="clr"></div>
        </div>
        <div class="text">
            Copyright © 2006-2026  <a href="http://www.shsxt.com">上海马士兵教育</a>  All Rights Reserved
        </div>
    </div>
</div>
</body>
</html>


