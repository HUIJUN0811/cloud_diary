package com.msbjy.note.service;

import cn.hutool.core.util.StrUtil;
import com.msbjy.note.dao.BaseDao;
import com.msbjy.note.dao.NoteDao;
import com.msbjy.note.po.Note;
import com.msbjy.note.util.Page;
import com.msbjy.note.vo.NoteVo;
import com.msbjy.note.vo.ResultInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteService {
    private NoteDao noteDao=new NoteDao();


    public ResultInfo<Note> addOrUpdat(String typeId, String title, String content,String noteId) {
        ResultInfo<Note> resultInfo=new ResultInfo<>();
        if (StrUtil.isBlank(typeId)){
            resultInfo.setCode(0);
            resultInfo.setMsg("类型ID为空");
            return  resultInfo;
        }
        if (StrUtil.isBlank(title)){
            resultInfo.setCode(0);
            resultInfo.setMsg("标题为空");
            return  resultInfo;
        }
        if (StrUtil.isBlank(content)){
            resultInfo.setCode(0);
            resultInfo.setMsg("内容为空");
            return  resultInfo;
        }
        //设置回显对象
        Note note=new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTypeId(Integer.parseInt(typeId));
        //判断noteId是否为空
        if (!StrUtil.isBlank(noteId)){
            note.setNoteId(Integer.parseInt(noteId));
        }
        resultInfo.setResult(note);

        int row = noteDao.addOrUpdate(note);
//        3. 判断受影响的行数
//        如果大于0，code=1
        if(row>0){
            resultInfo.setCode(1);
        }else {
//        如果不大于0，code=0，msg=xxx，result=note对象
            resultInfo.setCode(0);
            resultInfo.setMsg("更新失败");
            resultInfo.setResult(note);
        }
        return resultInfo;
    }


    public Page<Note>  findNoteListByPage(String pageNumStr, String pageSizeStr, Integer userId,String title,String date, String typeId) {
        // 设置分页参数的默认值
        Integer pageNum = 1; // 默认当前页是第一页
        Integer pageSize = 5; // 默认每页显示5条数据
        // 1. 参数的非空校验 （如果参数不为空，则设置参数）
        if (!StrUtil.isBlank(pageNumStr)) {
            // 设置当前页
            pageNum = Integer.parseInt(pageNumStr);
        }
        if (!StrUtil.isBlank(pageSizeStr)) {
            // 设置每页显示的数量
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 2. 查询当前登录用户的云记数量，返回总记录数 （long类型）
        long count = noteDao.findNoteCount(userId, title,date,typeId);

        // 3. 判断总记录数是否大于0
        if (count < 1) {
            return null;
        }

        // 4. 如果总记录数大于0，调用Page类的带参构造，得到其他分页参数的值，返回Page对象
        Page<Note> page = new Page<>(pageNum, pageSize, count);

        // 得到数据库中分页查询的开始下标
        Integer index = (pageNum -1) * pageSize;

        // 5. 查询当前登录用户下当前页的数据列表，返回note集合
        List<Note> noteList = noteDao.findNoteListByPage(userId, index, pageSize, title,date,typeId);

        // 6. 将note集合设置到page对象中
        page.setDataList(noteList);

        // 7. 返回Page对象
        return page;
    }

    public List<NoteVo> findNoteCountByDate(Integer userId) {
        return noteDao.findNoteCountByDate(userId);
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        return noteDao.findNoteCountByType(userId);
    }

    public Note findNoteById(String noteId) {
        if(StrUtil.isBlank(noteId)){
            return null;
        }
        Note note=noteDao.findNoteById(noteId);
        return note;
    }

    public Integer deleteNoteById(String noteId) {
        if (StrUtil.isBlank(noteId)){
            return 0;
        }
        int row= noteDao.deleteNoteById(noteId);
        if (row>0){
            return 1;
        }else {
            return  0;
        }
    }


    public ResultInfo<Map<String, Object>> queryNoteCountByMonth(Integer userId) {
        ResultInfo<Map<String, Object>> resultInfo=new ResultInfo<>();
        List<NoteVo> noteVos=noteDao.findNoteCountByDate(userId);
        if(noteVos !=null && noteVos.size()>0){
            List<String> mothList=new ArrayList<>();
            List<Integer> noteCountList=new ArrayList<>();

            for (NoteVo noteVo : noteVos) {
                mothList.add(noteVo.getGroupName());
                noteCountList.add((int)noteVo.getNoteCount());

            }
            Map<String,Object> map=new HashMap<>();
            map.put("monthArray",mothList);
            map.put("dataArray",noteCountList);
            resultInfo.setCode(1);
            resultInfo.setResult(map);

        }
        return resultInfo;
    }
}
