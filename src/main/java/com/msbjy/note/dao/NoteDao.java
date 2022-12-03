package com.msbjy.note.dao;

import cn.hutool.core.util.StrUtil;
import com.msbjy.note.po.Note;
import com.msbjy.note.vo.NoteVo;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    public int addOrUpdate(Note note) {
       String sql = "";
        List<Object> params = new ArrayList<>();
        params.add(note.getTypeId());
        params.add(note.getTitle());
        params.add(note.getContent());
        //判断noteId是否为空
        if (note.getNoteId()==null){
            sql="insert into tb_note (typeId, title, content, pubTime) values (?,?,?,now())";
        }else {
            sql="update tb_note set typeId= ? ,title= ? , content = ? where noteId = ? ";
            params.add(note.getNoteId());
        }
        int row=BaseDao.executeUpdate(sql,params);
        return row;
    }



    public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title,String date,String typeId) {
        String sql="select noteId,title,pubTime from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=? ";
        List params=new ArrayList();
        params.add(userId);
        if (!StrUtil.isBlank(title)){
            sql+="and title like concat('%',?,'%')";
            params.add(title);
        } else if (!StrUtil.isBlank(date)){
            sql+=" and date_format(pubTime,'%Y年%m月')=?";
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId=? ";
            params.add(typeId);
        }
        sql+=" limit ?,?";
        params.add(index);
        params.add(pageSize);
        List<Note> noteList=BaseDao.queryRows(sql,params,Note.class);
        return noteList;
    }

    public List<NoteVo> findNoteCountByDate(Integer userId) {
        // 定义SQL语句
        String sql = "SELECT count(1) noteCount,DATE_FORMAT(pubTime,'%Y年%m月') groupName FROM tb_note n " +
                " INNER JOIN tb_note_type t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY DATE_FORMAT(pubTime,'%Y年%m月')" +
                " ORDER BY DATE_FORMAT(pubTime,'%Y年%m月') DESC ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    public List<NoteVo> findNoteCountByType(Integer userId) {
        // 定义SQL语句
        String sql = "SELECT count(noteId) noteCount, t.typeId, typeName groupName FROM tb_note n " +
                " RIGHT JOIN tb_note_type t ON n.typeId = t.typeId WHERE userId = ? " +
                " GROUP BY t.typeId ORDER BY COUNT(noteId) DESC ";

        // 设置参数
        List<Object> params = new ArrayList<>();
        params.add(userId);

        // 调用BaseDao的查询方法
        List<NoteVo> list = BaseDao.queryRows(sql, params, NoteVo.class);

        return list;
    }

    public long findNoteCount(Integer userId, String title,String date,String typeId) {
        String sql="select count(1) from tb_note n inner join tb_note_type t on n.typeId=t.typeId where userId=?";
        List params=new ArrayList();
        params.add(userId);
        if (!StrUtil.isBlank(title)){
            sql+=" and title like concat('%',?,'%') ";
            params.add(title);
        } else if (!StrUtil.isBlank(date)){
            sql+=" and date_format(pubTime,'%Y年%m月')=?";
            params.add(date);
        }else if (!StrUtil.isBlank(typeId)) {
            sql += " and n.typeId=? ";
            params.add(typeId);
        }

        long count= (long) BaseDao.findSingleValue(sql,params);
        return count;
    }

    public Note findNoteById(String noteId) {
        String sql="select noteId,title,content,pubTime,typeName,n.typeId from tb_note n"+" inner join tb_note_type t on n.typeId=t.typeId where noteId= ?";
        List params=new ArrayList();
        params.add(noteId);
        Note note= (Note) BaseDao.querRow(sql,params,Note.class);
        return note;
    }

    public int deleteNoteById(String noteId) {
        String sql="delete from tb_note where noteId=? ";
        List params=new ArrayList();
        params.add(noteId);
        int row=BaseDao.executeUpdate(sql,params);
        return row;

    }
}
