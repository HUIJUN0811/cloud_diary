package com.msbjy.note.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    //得到配置文件对象
    private static Properties properties=new Properties();
    static {
        //加载配置文件
        InputStream in=DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
        /// 通过load()方法将输入流的内容加载到配置文件对象中
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static Connection getConnection(){
        Connection connection=null;
        try {
            String dbUrl=properties.getProperty("dbUrl");
            String dbName=properties.getProperty("dbName");
            String dbPwd=properties.getProperty("dbPwd");
            connection= DriverManager.getConnection(dbUrl,dbName,dbPwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void close(ResultSet resultSet,
                             PreparedStatement preparedStatement,
                             Connection connection) {

        try {
            // 判断资源对象如果不为空，则关闭
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
