package com.msbjy.note.test;


import com.msbjy.note.dao.BaseDao;
import com.msbjy.note.dao.UserDao;
import com.msbjy.note.po.NoteType;
import com.msbjy.note.po.User;
import com.msbjy.note.util.DBUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class Deom01 {

    @Test
    public void getTest(){
        System.out.println(DBUtil.getConnection());
    }
    @Test
    public void getTest2(){
        UserDao userDao=new UserDao();
        User user=userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());

    }
    @Test
    public void getTest3(){
      String sql="insert into tb_user (uname,upwd,nick,head,mood) values (?,?,?,?,?)";
        List<Object> params=new ArrayList<>();
        params.add("lishi1");
        params.add("25f9e794323b453885f5181f1b624d0b");
        params.add("lishi");
        params.add("404.jpg");
        params.add("Hello");
        int i = BaseDao.executeUpdate(sql, params);
        System.out.println(i);

    }
    @Test
    public void Test01(){
        String sql="select * from tb_note_type where userId=?";
        List<Object> params=new ArrayList<>();
        params.add(1);
        List<NoteType> list=BaseDao.queryRows(sql,params,NoteType.class);
        System.out.println(list);

    }
    @Test
    public void  getTest02(){
        String sql="select count(1) from tb_note where typeId=?";
        List params=new ArrayList();
        params.add(1);
        long count= (long) BaseDao.findSingleValue(sql,params);
        System.out.println(count);

    }
    @Test
    public void  getTest03(){
        String sql="delete  from tb_note_type where typeId=?";
        List params=new ArrayList();
        params.add(3);
        int row=BaseDao.executeUpdate(sql,params);
        System.out.println(row);

    }
}
