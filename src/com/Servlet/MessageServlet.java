package com.Servlet;

import com.DBTool.DBUtil;
import com.bean.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "MessageServlet")
public class MessageServlet extends HttpServlet {
    private Gson gson;//gson 对象用来序列化，反序列化json
    private   PreparedStatement preparedStatement; //准备执行SQL语句对象
    private ResultSet resultSet;//ResultSet结果集  ResultSet 数据库查询结果存储类
     private  Connection connection;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
               response.setContentType("application/json; charset=utf-8");
                String selfId= request.getParameter("selfId");//这个参数用来看自己有没有消息
                 System.out.println("selfid:"+selfId+"|");

                //这几个参数用来发送消息
                String sender=request.getParameter("sender");
                String receiver=  request.getParameter("receiver");
                String  msg=request.getParameter("msg");
        PrintWriter out=response.getWriter();//得到输出字符流
        ArrayList<Message> msgList = new ArrayList<Message>();//存储消息对象的动态数组
        Message cmsg;//消息类对象

      System.out.println(msg);

        /*
         消息发送处理
         */
        if(sender!=null&&receiver!=null)
        try {
            connection= DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
            int senderId=Integer.parseInt(sender);//注意这里有没有numberFormatException
            int receiverId=Integer.parseInt(receiver);
            String sql="insert into message(sender,receiver,msg,issend) values  (?,?,?,0)";//插入消息sql语句
            preparedStatement=connection.prepareStatement(sql);//准备执行这个数据库语句
            preparedStatement.setInt(1,senderId);//设置它的receiver参数为客户端发来的那个id
            preparedStatement.setInt(2,receiverId);
            preparedStatement.setString(3,msg);
            preparedStatement.executeUpdate();//执行插入操作
            System.out.println("插入成功");

            preparedStatement.close();//关闭这个对象
        }
        catch (NumberFormatException e) {
            System.out.println("sender receiver必须是数字");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(connection!=null)
                    connection.close();//关闭数据库连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




        if(selfId!=null) {
        /*
         消息接收处理，查询有没有消息，接收的消息设置为已读
         */
            try {
                connection = DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
                int selfid = Integer.parseInt(selfId);//注意这里有没有numberFormatException
                String sql = "select * from message where receiver=? and issend=0";//看看有没有消息可以接收
                preparedStatement = connection.prepareStatement(sql);//准备执行这个数据库语句
                preparedStatement.setInt(1, selfid);//设置它的receiver参数为客户端发来的那个id
                resultSet = preparedStatement.executeQuery();//执行查询语句并拿到结果的对象
                while (resultSet.next())//看结果集里有结果吗
                {
                    // System.out.println("有显示吗");
                    cmsg = new Message();//初使化一个Message对象 ，用来存拿出来的结果
                    try {
                        cmsg.setSender(Integer.parseInt(resultSet.getString("sender")));
                        ;//得到发送者是谁
                    } catch (NumberFormatException e) //防止id出错
                    {
                        e.printStackTrace();
                    }
                    cmsg.setMsg(resultSet.getString("msg"));//得到消息
                    msgList.add(cmsg);//把这一个消息加入list中
                }
                gson = new GsonBuilder().create();//创造一个Gson对象
                String json = gson.toJson(msgList);//通过gson把数据转成json格式
                out.println(json);//把数据传到客户端
                //把发送过的消息设置为已读
                sql = "update message set issend=1 where receiver=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, selfid);
                preparedStatement.executeUpdate();//执行更新语句
                resultSet.close();//关闭结果集对象
                preparedStatement.close();//关闭这个对象
            } catch (NumberFormatException e) {
                System.out.println("selfid 必须是数字");
            } catch (Exception e) {
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
            doPost(request,response);//get请求方式直接调用post方式
    }
}
