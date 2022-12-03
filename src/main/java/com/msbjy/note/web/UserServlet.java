package com.msbjy.note.web;

import com.msbjy.note.po.User;
import com.msbjy.note.service.UserService;
import com.msbjy.note.vo.ResultInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.CloseShieldOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {
    private UserService userService=new UserService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 接收用户行为
        String actionName = request.getParameter("actionName");
        // 判断用户行为，调用对应的方法
        if ("login".equals(actionName)) {

            // 用户登录
            userLogin(request, response);

        } else if ("logout".equals(actionName)) {

            // 用户退出
            userLogOut(request, response);

        } else if ("userCenter".equals(actionName)) {

            // 进入个人中心
            userCenter(request, response);

        } else if ("userHead".equals(actionName)) {

            // 加载头像
            userHead(request, response);

        } else if ("checkNick".equals(actionName)) {

            // 验证昵称的唯一性
            checkNick(request, response);

        } else if ("updateUser".equals(actionName)) {

            // 修改用户信息
            updateUser(request, response);

        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo=userService.updateUser(request);
        request.setAttribute("resultInfo",resultInfo);
        request.getRequestDispatcher("user?actionName=userCenter").forward(request,response);
    }

    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取参数
      String nick=request.getParameter("nick");
      User user= (User) request.getSession().getAttribute("user");
      Integer code = userService.checkNick(nick, user.getUserId());
      response.getWriter().write(code);
      response.getWriter().close();




    }

    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String head=request.getParameter("imageName");
        String path=request.getServletContext().getRealPath("/WEB-INF/upload/");
        File file=new File(path+"/"+head);
        String pic= head.substring(head.lastIndexOf(".")+1);
        if("png".equalsIgnoreCase(pic)){
            response.setContentType("image/png");
        }else if ("jpg".equalsIgnoreCase(pic) ||"jpeg".equalsIgnoreCase(pic)){
            response.setContentType("image/jpeg");
        }else  if ("gif".equalsIgnoreCase(pic)){
            response.setContentType("image/gif");
        }
        FileUtils.copyFile(file, response.getOutputStream());
    }

    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("changePage","user/info.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        Cookie cookie=new Cookie("user",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect("login.jsp");
    }

    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //获取用户名和密码
        String uname=request.getParameter("userName");
        String pwd=request.getParameter("userPwd");
        //调用Service层的方法，返回ResultInfo对象
         ResultInfo<User> resultInfo=userService.userLogin(uname,pwd);
         if (resultInfo.getCode()==1){
             //  将用户信息设置到session作用域中
             request.getSession().setAttribute("user", resultInfo.getResult());
             //  判断用户是否选择记住密码（rem的值是1）
             String rem = request.getParameter("rem");
             // 如果是，将用户姓名与密码存到cookie中，设置失效时间，并响应给客户端
             if ("1".equals(rem)) {
                 // 得到Cookie对象
                 Cookie cookie = new Cookie("user",uname +"-"+pwd);
                 // 设置失效时间
                 cookie.setMaxAge(3*24*60*60);
                 // 响应给客户端
                 response.addCookie(cookie);
         }else {
                 Cookie cookie=new Cookie("user",null);
                 cookie.setMaxAge(0);
                 response.addCookie(cookie);
         }
             response.sendRedirect("index");
    }else {
             request.getSession().setAttribute("resultInfo", resultInfo);
             request.getRequestDispatcher("login.jsp").forward(request,response);
         }
    }
}
