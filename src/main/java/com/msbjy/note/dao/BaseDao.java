package com.msbjy.note.dao;



import com.msbjy.note.util.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaseDao {


    public  static int executeUpdate(String sql, List<Object> params){
        int row = 0; // 受影响的行数
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 得到数据库连接
            connection = DBUtil.getConnection();
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 如果有参数，则设置参数，下标从1开始
            if (params != null && params.size() > 0) {
                // 循环设置参数，设置参数类型为Object
                for (int i = 0; i < params.size(); i++){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }
            // 执行更新，返回受影响的行数
            row = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            DBUtil.close(null, preparedStatement, connection);
        }

        return row;
    }

    public static Object findSingleValue(String sql,List<Object> params){
        Object object = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 获取数据库连接
            connection = DBUtil.getConnection();
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 如果有参数，则设置参数，下标从1开始
            if (params != null && params.size() > 0) {
                // 循环设置参数，设置参数类型为Object
                for (int i = 0; i < params.size(); i++){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }
            // 执行查询，返回结果集
            resultSet = preparedStatement.executeQuery();
            // 判断并分析结果集
            if (resultSet.next()) {
                object = resultSet.getObject(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            DBUtil.close(resultSet, preparedStatement, connection);
        }

        return object;
    }

    public static List queryRows(String sql, List<Object> params,Class cls){
        List list=new ArrayList();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try{
        connection=DBUtil.getConnection();
        preparedStatement=connection.prepareStatement(sql);
        if (params!=null && params.size()>0){
            for (int i =0; i <params.size() ; i++) {
                preparedStatement.setObject(i+1,params.get(i));
            }
        }
        resultSet= preparedStatement.executeQuery();
        //得到结果集的元数据对象
            ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
            //得到查询的字段数量
            int fideNum=resultSetMetaData.getColumnCount();
            while (resultSet.next()){
                 Object object=cls.newInstance();
                for (int i = 1; i <= fideNum; i++) {
                    String columName=resultSetMetaData.getColumnLabel(i);
                    Field field=cls.getDeclaredField(columName);
                    String setMethod="set"+columName.substring(0,1).toUpperCase()+columName.substring(1);
                    Method method=cls.getDeclaredMethod(setMethod,field.getType());
                    Object value=resultSet.getObject(columName);
                    method.invoke(object,value);
                }
                list.add(object);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  list;
    }


    public  static  Object querRow(String sql, List<Object> params,Class cls){
             List list=queryRows(sql,params,cls);
             Object object=null;
             if(list !=null && list.size()>0){
                 object=list.get(0);
             }
             return object;
    }

}
