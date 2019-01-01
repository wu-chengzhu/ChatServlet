package com.Servlet;

import com.DBTool.DBUtil;
import com.bean.User;
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
import java.sql.SQLException;

@WebServlet(name = "AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
    private Gson gson;//gson 对象用来序列化，反序列化json
    private PreparedStatement preparedStatement; //准备执行SQL语句对象
    private ResultSet resultSet;//ResultSet结果集  ResultSet 数据库查询结果存储类
    private Connection connection;
    private  int userID;
    private  User friend;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        String addAction=request.getParameter("action");//有要加的动作
        String userid=request.getParameter("userid");
        String friendname=request.getParameter("friendName");//得到要加的那个好友的用户名

        PrintWriter out=response.getWriter();//得到输出字符流


        if(addAction!=null)//如果是add的动作就在数据库中插入这几个操作
        {

            try {
                gson = new Gson();//new一个gson对象
                connection = DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接

                String sql = "insert into friendlist(userid,friendid) values (?,?),(?,?)";
                preparedStatement = connection.prepareStatement(sql);//准备执行这个数据库语句
                preparedStatement.setInt(1,userID);
                preparedStatement.setInt(2,friend.getId());
                preparedStatement.setInt(3,friend.getId());
                preparedStatement.setInt(4,userID);
                preparedStatement.executeUpdate();//执行查询结果

               out.println(gson.toJson(true));

                //out.println(addResultJ);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                out.println(gson.toJson(false));//失败
                e.printStackTrace();
            } finally {

                try {
                    if (connection != null)
                        connection.close();//关闭数据库连接
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }


        if(userid!=null)//把用户的id发过来就把那个用户的信息发回去
        {
            userID=Integer.parseInt(userid);
            System.out.println(userid);
            System.out.println(friend.toString());
            out.println(gson.toJson(friend));
        }




       if(friendname!=null) {//输了要加的人的用户名才给它这个信息
           try {
               gson = new Gson();//new一个gson对象
               String addResultJ;//加好友结果
               connection = DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
               String sql = "select * from user where username=?";
               preparedStatement = connection.prepareStatement(sql);//准备执行这个数据库语句
               preparedStatement.setString(1, friendname);
               resultSet = preparedStatement.executeQuery();//执行查询结果

               if (resultSet.next())//如果有就取出他的id
               {
                   friend = new User();

                   friend.setId(resultSet.getInt("id"));
                   //取出这个朋友的id然后存起来
                   friend.setUsername(resultSet.getString("username").toString());//取出这个朋友的username然后存起来
                   //  friend.setPassword(resultSet.getString("password").toString());//取出这个朋友的password然后存起来
                   addResultJ = gson.toJson(true);//返回客户端有这个用户

               } else {
                   addResultJ = gson.toJson(false);//没有这个用户
               }

               out.println(addResultJ);
           } catch (SQLException e) {
               e.printStackTrace();
           } catch (Exception e) {
               //out.println("有错误");
               e.printStackTrace();
           } finally {

               try {
                   if (connection != null)
                       connection.close();//关闭数据库连接
               } catch (SQLException e) {
                   e.printStackTrace();
               }

           }
       }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
