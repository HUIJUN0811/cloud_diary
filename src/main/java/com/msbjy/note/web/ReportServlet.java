package com.msbjy.note.web;

import com.msbjy.note.po.User;
import com.msbjy.note.service.NoteService;
import com.msbjy.note.util.JsonUtil;
import com.msbjy.note.vo.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private NoteService noteService=new NoteService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String actionName=request.getParameter("actionName");
        if ("info".equals(actionName)){
            reportInfo(request,response);
        }else  if ("month".equals(actionName)){
            queryNoteCountByMonth(request,response);
        }
    }

    private void queryNoteCountByMonth(HttpServletRequest request, HttpServletResponse response) {
        User user= (User) request.getSession().getAttribute("user");
        ResultInfo<Map<String,Object>> resultInfo=noteService.queryNoteCountByMonth(user.getUserId());
        JsonUtil.toJson(response,resultInfo);

    }

    private void reportInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         request.setAttribute("changePage","report/info.jsp");
         request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
