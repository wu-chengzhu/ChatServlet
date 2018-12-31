////package com.Servlet;
////import com.DBTool.DBUtil;
////import com.google.gson.Gson;
////import com.google.gson.GsonBuilder;
////
////import javax.servlet.http.HttpServlet;
////import java.io.IOException;
////import java.io.PrintWriter;
////import java.sql.*;
////
////public class Login extends HttpServlet {
////
////
////    public Login() {
////      super();
////    }
////
////    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
////            throws javax.servlet.ServletException, IOException {
////
////        response.setCharacterEncoding("utf-8");//设置响应的字符集
////        String uname=request.getParameter("user");//用户名参数
////        String pwd=request.getParameter("pass");//密码参数
////        PrintWriter pw=response.getWriter();
////        Gson gson=new GsonBuilder().create();//创造一个Gson对象
////
////        int userId=3;
////        if(userId>0)
////        {
////            request.getSession(true).setAttribute("userId",userId);//设置session
////        }
////         String json=gson.toJson(userId);//通过gson把数据转成json格式
////        pw.print(json);//传给客户端
////
////
////
//
//
//
////        String ID=request.getParameter("ID");//用于接收前段输入的ID值，此处参数须和input控件的name值一致
////        String PW=request.getParameter("PW");//用于接收前段输入的PW值，此处参数须和input控件的name值一致
////       String Login=request.getParameter("Login");
////        boolean type=false;//用于判断账号和密码是否与数据库中查询结果一致
////        response.setContentType("text/html;charset=UTF-8");//设置数据类型和字符集类型
////        PrintWriter out=response.getWriter(); //字符打印输出流
////        try{
////
////            Connection con=DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
////            Statement stmt=con.createStatement();//创建一个SQL语句对象
////                String sql="select * from demodatabase.demotable where uid='"+ID+"' and pwd="+PW;//数据库查询语句
////                ResultSet rs=stmt.executeQuery(sql);//执行查询并把结果返回给结果集rs
////                while(rs.next())//看结果集是否有结果
////                {
////                    type=true;
////                }
////                out.print("登录结果:"+type);//输出有没有这个用户
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        finally
////            {
////                out.flush();
////                out.close();
////            }
////            out.close();
//
////    }
////
////    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
////                doPost(request,response);
////    }
////}
//
//package com.Servlet;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//
//@WebServlet(name = "SQLTestServlet")
//public class Login extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request, response);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String username=request.getParameter("user");
//        String password=request.getParameter("pass");
//        response.setContentType("text/json; charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demodatabase?serverTimezone=UTC", "root", "w7387868.");
//
//            boolean isuser=false;
//            String sql="select * from demodatabase.demotable where uid=? and pwd=?";//数据库查询语句
//            PreparedStatement preparedStatement=connection.prepareStatement(sql);
//            writer.println(username);
//            writer.println(password);
//            preparedStatement.setString(1,username);
//            preparedStatement.setString(2,password);
//            ResultSet resultSet = preparedStatement.executeQuery();
//              while(resultSet.next())
//              {
//                  isuser=true;
//                 // writer.print("数据库里么有");
//              }
//              System.out.println("是用户吗"+isuser);
//            Gson gson=new GsonBuilder().create();//创造一个Gson对象
//
//            String json=gson.toJson(isuser,Boolean.class);//通过gson把数据转成json格式
//
//            System.out.println("user的名字是"+username);
//            System.out.println("password是"+password);
//
////            JSONArray jsonArray = new JSONArray();
////            JSONObject jsonObject = new JSONObject();
////            while (resultSet.next()) {
////                jsonObject.put("uid", resultSet.getString("abcs"));
////                jsonObject.put("pwd", resultSet.getString("4567"));
////                jsonObject.put("column", resultSet.getString("5"));
////                jsonArray.add(jsonObject);
////            }
//            writer = response.getWriter();
//            writer.println(json);
//            resultSet.close();
//            preparedStatement.close();//预编译数据库语句对象关闭
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//


package com.Servlet;
import com.DBTool.DBUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Map;

@WebServlet(name = "Login")
public class Login extends HttpServlet {
    private int id=-1;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        request.setCharacterEncoding("utf-8");
        BufferedReader reader=request.getReader();//得到request中的文件读取流
        String  receivejson=reader.readLine();
        System.out.println(receivejson);
        Gson  rgson=new GsonBuilder().enableComplexMapKeySerialization().create();
         Type type=new TypeToken<Map<String,String>>(){}.getType();//说明数据类型
        Map<String,String> map=rgson.fromJson(receivejson,type);
        PrintWriter writer = response.getWriter();
        //writer.println(map.get("user"));
        //writer.println(map.get("pass"));
        String username=map.get("user");//从map中取出user的数据
       String password=map.get("pass");//取出pass数据

        try {

            Connection connection= DBUtil.getConnection();//调用DBUtil类的函数建立数据库连接
            boolean isuser=false;
            String sql="select * from demodatabase.user where username=? and password=?";//数据库查询语句
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();//执行查询语句
            while(resultSet.next())
            {
              id=resultSet.getInt("id");
            }
          //  System.out.println("是用户吗"+id);
            Gson gson=new GsonBuilder().create();//创造一个Gson对象

            String json=gson.toJson(id,Integer.class);//通过gson把数据转成json格式
            id=-1;//恢复默认值
            writer.println(json);//发送数据
            //把这些与数据库操作相关对象都关掉
            resultSet.close();
            preparedStatement.close();
            connection.close();//关闭数据库连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
