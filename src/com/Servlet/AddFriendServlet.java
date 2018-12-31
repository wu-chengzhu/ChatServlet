package com.Servlet;

import com.DBTool.DBUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    private Gson gson;//gson 对象用来序列化，反序列化json
    private PreparedStatement preparedStatement; //准备执行SQL语句对象
    private ResultSet resultSet;//ResultSet结果集  ResultSet 数据库查询结果存储类
    private Connection connection;
    private  int userID;
    private int friendID;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        String userid=request.getParameter("userid");
        String username=request.getParameter("friendName");//得到要加的那个好友的用户名

        PrintWriter out=response.getWriter();//得到输出字符流
        try {
            connection= DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
            userID=Integer.parseInt(userid);
            String sql="select * from user where username=?";
            preparedStatement=connection.prepareStatement(sql);//准备执行这个数据库语句
            preparedStatement.setString(1,username);
            resultSet=preparedStatement.executeQuery();//执行查询结果
            gson=new Gson();//new一个gson对象
            String addResultJ;//加好友结果
            if(resultSet.next())//如果有就取出他的id
            {
                resultSet.getInt("id");//取出这个朋友的id




            }
            else
            {
                addResultJ=gson.toJson(false,Boolean.class);//没有这个用户 加好友失败

            }

            out.println();



        } catch (Exception e) {

            e.printStackTrace();
        } finally {



        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
