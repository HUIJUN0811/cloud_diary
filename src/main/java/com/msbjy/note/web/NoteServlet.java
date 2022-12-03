package com.msbjy.note.web;

import cn.hutool.core.util.StrUtil;
import com.msbjy.note.po.Note;
import com.msbjy.note.po.NoteType;
import com.msbjy.note.po.User;
import com.msbjy.note.service.NoteService;
import com.msbjy.note.service.NoteTypeService;
import com.msbjy.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    private NoteService noteService=new NoteService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String actionName=request.getParameter("actionName");
         if("view".equals(actionName)){
             noteView(request,response);
         }else if ("addOrUpdate".equals(actionName)){
             addOrUpdate(request,response);
         } else if ("detail".equals(actionName)){
             notedetail(request,response);
         } else if ("delete".equals(actionName)){
             notedelete(request,response);
         }
    }

    private void notedelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String noteId=request.getParameter("noteId");
       Integer code=noteService.deleteNoteById(noteId);
        response.getWriter().write(code+"");
        response.getWriter().close();

    }

    private void notedetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String noteId=request.getParameter("noteId");
        Note note=noteService.findNoteById(noteId);
        request.setAttribute("note",note);
        request.setAttribute("changePage","note/detail.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);


    }

    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //接收参数
        String typeId=request.getParameter("typeId");
        String title=request.getParameter("title");
        String content=request.getParameter("content");
        //如果是修改操作，需要接收noteId
        String noteId=request.getParameter("noteId");
        //调用service方法
        ResultInfo<Note> resultInfo=noteService.addOrUpdat(typeId,title,content,noteId);
        if (resultInfo.getCode()==1) {
//        如果code=1，表示成功
//        重定向跳转到首页 index
            response.sendRedirect("index");
        }else {
//        如果code=0，表示失败
//                将resultInfo对象设置到request作用域
//        请求转发跳转到note?actionName=view
            request.setAttribute("resultInfo",resultInfo);
            String url="note?actionName=view";
            if(StrUtil.isBlank(noteId)){
                url+="&noteId="+noteId;
            }
            request.getRequestDispatcher(url).forward(request,response);
        }

    }

    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String noteId=request.getParameter("noteId");
        Note note=noteService.findNoteById(noteId);
        request.setAttribute("noteInfo",note);
        User user= (User) request.getSession().getAttribute("user");
        List<NoteType> typeList=new NoteTypeService().findTypeList(user.getUserId());
        request.setAttribute("typeList",typeList);
        request.setAttribute("changePage","note/view.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);

    }
}
