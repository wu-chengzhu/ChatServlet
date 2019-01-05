package com.DBTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    private static String url="jdbc:mysql://localhost:3306/demodatabase?serverTimezone=UTC";
    private static String driverClass="com.my.cj.jdbc.Driver";
    private static String username="root";
    private static  String password="w7387868.";
    private static Connection conn;//数据库连接
    //装载驱动
    static{
        try{
            Class.forName(driverClass);//加载驱动
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
  // 获得数据库的连接 用函数封装了数据库的连接
    public  static Connection getConnection(){
         try{
             conn= DriverManager.getConnection(url,username,password);//拿到这个连接的对象
         }
         catch (SQLException e)
         {
             e.printStackTrace();
         }
         return  conn;//返回这个数据库连接对象
    }

    //在main函数中调用那个函数 建立数据库连接
    public static void main(String []args)
    {
        Connection conn=DBUtil.getConnection();//
        if(conn==null)
        {
            System.out.println("数据库连接失败");
        }
    }
    public static  void close(){
        if(conn!=null)//如果数据库连接对象不为空就关闭它
        {
            try{
                conn.close();
            }
            catch (SQLException e)
            {
                System.out.println("数据库关闭失败");
            }
        }
    }


}
