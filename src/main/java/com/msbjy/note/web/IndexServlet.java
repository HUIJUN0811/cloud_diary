package com.msbjy.note.web;

import com.msbjy.note.po.Note;
import com.msbjy.note.po.User;
import com.msbjy.note.service.NoteService;
import com.msbjy.note.util.Page;
import com.msbjy.note.vo.NoteVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actionName=request.getParameter("actionName");
        request.setAttribute("action",actionName);
        if("searchTitle".equals(actionName)){
            String title=request.getParameter("title");
            request.setAttribute("title",title);
            noteList(request,response,title,null,null);
        }else  if ("searchDate".equals(actionName)){
            String date=request.getParameter("date");
            request.setAttribute("date",date);
            noteList(request,response,null, date,null);
        }else if ("searchType".equals(actionName)){
            String typeId=request.getParameter("typeId");
            request.setAttribute("typeId",typeId);
            noteList(request,response,null,null,typeId);
        }
        else {
            noteList(request, response,null,null,null);
        }
        request.setAttribute("changePage","note/list.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    private void noteList(HttpServletRequest request, HttpServletResponse response,String title,String date,String typeId) {
        // 1. 接收参数 （当前页、每页显示的数量）
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");

        // 2. 获取Session作用域中的user对象
        User user = (User) request.getSession().getAttribute("user");

        // 3. 调用Service层查询方法，返回Page对象
        Page<Note> page = new NoteService().findNoteListByPage(pageNum, pageSize, user.getUserId(), title,date,typeId);

        // 4. 将page对象设置到request作用域中
        request.setAttribute("page", page);

        // 通过日期分组查询当前登录用户下的云记数量
        List<NoteVo> dateInfo = new NoteService().findNoteCountByDate(user.getUserId());
        // 设置集合存放在request作用域中
        request.getSession().setAttribute("dateInfo", dateInfo);

        // 通过类型分组查询当前登录用户下的云记数量
        List<NoteVo> typeInfo = new NoteService().findNoteCountByType(user.getUserId());
        // 设置集合存放在request作用域中
        request.getSession().setAttribute("typeInfo", typeInfo);

    }
}
