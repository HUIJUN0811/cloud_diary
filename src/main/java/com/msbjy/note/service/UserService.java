package com.msbjy.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.msbjy.note.dao.UserDao;
import com.msbjy.note.po.User;
import com.msbjy.note.vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;


//Service层：（业务逻辑层：参数判断、业务逻辑处理）
//        1. 判断参数是否为空
//        如果为空
//        设置ResultInfo对象的状态码和提示信息
//        返回resultInfo对象
//        2. 如果不为空，通过用户名查询用户对象
//        3. 判断用户对象是否为空
//        如果为空
//        设置ResultInfo对象的状态码和提示信息
//        返回resultInfo对象
//        4. 如果用户对象不为空，将数据库中查询到的用户对象的密码与前台传递的密码作比较 （将密码加密后再比较）
//        如果密码不正确
//        设置ResultInfo对象的状态码和提示信息
//        返回resultInfo对象
//        5. 如果密码正确
//        设置ResultInfo对象的状态码和提示信息
//        6. 返回resultInfo对象
public class UserService {
    UserDao userDao=new UserDao();

    public ResultInfo<User> userLogin(String uname, String pwd) {
        ResultInfo<User> resultInfo=new ResultInfo();
        User user=new User();
         user.setUname(uname);
         user.setUpwd(pwd);
         resultInfo.setResult(user);

      //1. 判断参数是否为空
        if(StrUtil.isBlank(uname)|| StrUtil.isBlank(pwd)){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名或者密码为空");
            return  resultInfo;
        }
        //2. 如果不为空，通过用户名查询用户对象
        User user1=userDao.queryUserByName(uname);
        if (user1==null){
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名不存在");
            return  resultInfo;
        }
        pwd= DigestUtil.md5Hex(pwd);
         if(!pwd.equals(user1.getUpwd())){
             resultInfo.setCode(0);
             resultInfo.setMsg("密码不正确");
             return  resultInfo;
         }
         resultInfo.setCode(1);
         resultInfo.setResult(user1);
        return resultInfo;
    }

    public   Integer checkNick(String nick, Integer userId) {
        //1. 判断昵称是否为空
        if(StrUtil.isBlank(nick)){
            return 0;
        }
        //2.2. 调用Dao层，通过用户ID和昵称查询用户对象
        User user=userDao.queryUserByIdNick(nick,userId);
        //3. 判断用户对象存在
        if(user !=null){
            return 0;
        }
        return 1;
    }

    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        // 1. 获取参数（昵称、心情）
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");

        // 2. 参数的非空校验（判断必填参数非空）
        if (StrUtil.isBlank(nick)) {
            // 如果昵称为空，将状态码和错误信息设置resultInfo对象中，返回resultInfo对象
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能卫空！");
            return resultInfo;
        }

        // 3. 从session作用域中获取用户对象（获取用户对象中默认的头像）
        User user = (User) request.getSession().getAttribute("user");
        // 设置修改的昵称和头像
        user.setNick(nick);
        user.setMood(mood);

        // 4. 实现上上传文件
        try {
            // 1. 获取Part对象 request.getPart("name"); name代表的是file文件域的name属性值
            Part part = request.getPart("img");
            // 2. 通过Part对象获取上传文件的文件名 (从头部信息中获取上传的文件名)
            String header = part.getHeader("Content-Disposition");
            System.out.println(header);
            // 获取具体的请求头对应的值
            String str = header.substring(header.lastIndexOf("=") + 2);
            // 获取上传的文件名
            String fileName = str.substring(0, str.length() - 1);
            // 3. 判断文件名是否为空
            if (!StrUtil.isBlank(fileName)) {
                // 如果用户上传了头像，则更新用户对象中的头像
                user.setHead(fileName);
                // 4. 获取文件存放的路径  WEB-INF/upload/目录中
                String filePath = request.getServletContext().getRealPath("/WEb-INF/upload/");
                // 5. 上传文件到指定目录
                part.write(filePath + "/" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 6. 调用Dao层的更新方法，返回受影响的行数
        int row = userDao.updateUser(user);
        // 7. 判断受影响的行数
        if (row > 0) {
            resultInfo.setCode(1);
            // 更新session中用户对象
            request.getSession().setAttribute("user", user);
        } else {
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败！");
        }

        return resultInfo;
    }
}
