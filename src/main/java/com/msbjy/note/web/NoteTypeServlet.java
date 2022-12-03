package com.msbjy.note.web;

import com.msbjy.note.po.NoteType;
import com.msbjy.note.po.User;
import com.msbjy.note.service.NoteTypeService;
import com.msbjy.note.util.JsonUtil;
import com.msbjy.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {

    private NoteTypeService noteTypeService=new NoteTypeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String actionName=request.getParameter("actionName");
       if ("list".equals(actionName)){
           typeList(request,response);
       }else if ("delete".equals(actionName)){
           delete(request,response);
       }else if("addOrUpdate".equals(actionName)){
           addOrUpdate(request,response);
       }
    }

    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        // 1. 接收参数 （类型名称、类型ID）
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");
        // 2. 获取Session作用域中的user对象，得到用户ID
        User user = (User) request.getSession().getAttribute("user");
        // 3. 调用Service层的更新方法，返回ResultInfo对象
        ResultInfo<Integer> resultInfo = noteTypeService.addOrUpdate(typeName, user.getUserId(), typeId);
        // 4. 将ResultInfo转换成JSON格式的字符串，响应给ajax的回调函数
        JsonUtil.toJson(response, resultInfo);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        //获取参数
        String typeId=request.getParameter("typeId");
        ResultInfo resultInfo=noteTypeService.delete(typeId);
        JsonUtil.toJson(response,resultInfo);

    }

    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取Session作用域设置的user对象
        User user= (User) request.getSession().getAttribute("user");
        //调用Service的查询方法，查询当前登录用户的类型集合，返回集合
      List<NoteType> typeList= noteTypeService.findTypeList(user.getUserId());
      request.setAttribute("typeList",typeList);
      //设置s首页动态包含的页面值
        request.setAttribute("changePage","type/list.jsp");
        // 请求跳转到index.jsp页面
      request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
