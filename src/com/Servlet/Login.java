

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
