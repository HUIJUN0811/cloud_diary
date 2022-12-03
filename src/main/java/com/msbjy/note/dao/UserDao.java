package com.msbjy.note.dao;

import com.msbjy.note.po.User;
import com.msbjy.note.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public User queryUserByName(String uname){
        User  user=null;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        //获取数据库连接
        connection= DBUtil.getConnection();
        //定义sql语句
        String sql="select * from tb_user where uname=?";
        try {
            //预编译
            preparedStatement=connection.prepareStatement(sql);
            //设置参数
            preparedStatement.setString(1,uname);
            //执行查询
            resultSet=preparedStatement.executeQuery();
            //判断并分析结果集
            if (resultSet.next()){
                user=new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUpwd(resultSet.getString("upwd"));
                user.setUname(uname);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(resultSet,preparedStatement,connection);
        }


        return  user;
    }

    public User queryUserByIdNick(String nick, Integer userId) {
        String sql="select * from tb_user where nick=? and userId !=?";
        List<Object> params=new ArrayList();
        params.add(nick);
        params.add(userId);
        User user= (User) BaseDao.querRow(sql,params,User.class);
        return user;
    }

    public int updateUser(User user) {
        // 1. 定义SQL语句
        String sql = "update tb_user set nick = ?, mood = ?, head = ? where userId = ? ";
        // 2. 设置参数集合
        List<Object> params = new ArrayList<>();
        params.add(user.getNick());
        params.add(user.getMood());
        params.add(user.getHead());
        params.add(user.getUserId());
        // 3. 调用BaseDao的更新方法，返回受影响的行数
        int row = BaseDao.executeUpdate(sql, params);
        return row;
    }
}
