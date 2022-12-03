package com.msbjy.note.dao;

import com.msbjy.note.po.Note;
import com.msbjy.note.po.NoteType;
import com.msbjy.note.util.DBUtil;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TypeDao {

    public static int deleteTypeById(String typeId) {
        // 定义SQL语句
        String sql = "delete from tb_note_type where typeId = ?";
        // 设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(typeId);
        // 调用BaseDao
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }

    public List<NoteType> findTypeListByUserId(Integer userId){
        String sql="select * from tb_note_type where userId=?";
        List<Object> params=new ArrayList<>();
        params.add(userId);
        List<NoteType> list=BaseDao.queryRows(sql,params,NoteType.class);
        return list;
    }


    public long findNoteCountByTypeId(String typeId) {
        // 通过类型ID查询云记记录的数量，返回云记数量
        String sql="select count(1) from tb_note where typeId=?";
        List params=new ArrayList();
        params.add(typeId);
        long count= (long) BaseDao.findSingleValue(sql,params);
        return count;


    }

    public Integer checkTypeName(String typeName, Integer userId, String typeId) {
        String sql="select * from tb_note_type where userId=? and typeName=?";
        List params=new ArrayList();
        params.add(userId);
        params.add(typeName);
        NoteType noteType= (NoteType) BaseDao.querRow(sql,params,NoteType.class);
        if(noteType==null){
            return 1;

        }else {
            if(typeId==noteType.getTypeId().toString()){
                return 1;
            }
        }
        return 0;
    }

    public Integer addType(Integer userId, String typeName) {
        Integer key = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "insert into tb_note_type (typeName,userId) values (?,?)";
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setObject(1,typeName);
            preparedStatement.setObject(2,userId);
            int row= preparedStatement.executeUpdate();
            if(row>0){
                resultSet = preparedStatement.getGeneratedKeys();
                // 得到主键的值
                if (resultSet.next()) {
                    key = resultSet.getInt(1);
                }
            }


        }catch (Exception e){
            e.printStackTrace();

        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }
        return  key;

    }

    public Integer updateType(String typeId, String typeName) {
        String sql = "update tb_note_type set typeName = ? where typeId = ?";
        List params=new ArrayList();
        params.add(typeName);
        params.add(typeId);
       int row= BaseDao.executeUpdate(sql,params);
       return  row;
    }
}
