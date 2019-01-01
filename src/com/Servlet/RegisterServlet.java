package com.Servlet;

import com.DBTool.DBUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private PreparedStatement sql;//准备执行SQL语句对象

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        response.setContentType("application/json; charset=utf-8");//设置响应类型和字符集
        request.setCharacterEncoding("utf-8");//设置请求的字符集
        PrintWriter writer = response.getWriter();// 拿到响应的字符打印输出流对象
        String username=request.getParameter("user");//得到客户端发过来的user参数
        String password=request.getParameter("pass");//得到客户端发过来的pass参数
        //   System.out.println("user的名字是"+username);
        //  System.out.println("password是"+password);
        try {
            Connection connection= DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
             boolean isuser=false;//判断是否是用户
            String selectsql="select * from user where username=?";//数据库查询语句
            sql=connection.prepareStatement(selectsql);//准备数据库执行语句
            sql.setString(1,username);
            ResultSet resultSet = sql.executeQuery();//执行查询语句并拿到结果
            String json;
            Gson gson=new GsonBuilder().create();//创造一个Gson对象;
            while(resultSet.next())//如果结果集里有数据
            {
                isuser=true;//是用户
            }
            if(isuser==true){//已经有这个用户
                json=gson.toJson(false);//通过gson把数据转成json格式 传给它错
            }
            else{ //不然就把这个用户的数据插入进去
                sql=connection.prepareStatement("INSERT INTO user (username,password) VALUES (?, ?)");
              //  sql.setInt(1,i++);
                sql.setString(1,username);
                sql.setString(2,password);
                sql.executeUpdate();//执行更新
                json=gson.toJson(true);//把数据转成json格式
                sql.close();
            }
         // Boolean isregister=false;
            writer.println(json);//把数据传给客户端
            isuser=false;
            //把这些都close掉
            sql.close();//
            resultSet.close();
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
