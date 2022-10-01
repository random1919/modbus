package com.example.demo.oracle;

import java.sql.*;

/**
 * 功能描述
 *
 * @author: 杜莉莎
 * @date: 2022年09月20日 13:46
 */
public class connectDemo {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //加载数据驱动
        ResultSet rs = null;

        Class.forName("oracle.jdbc.driver.OracleDriver");

        String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
        String user = "moyu";
        String password = "20001226";
        Connection connection = DriverManager.getConnection(url, user, password);

        Statement statement = connection.createStatement();
        String sql = "select * from " + '"' +  "point" +'"';
        rs = statement.executeQuery(sql);
        while(rs.next()) {  //循环遍历结果集
            int id= rs.getInt("ID");
            String name= rs.getString("DAP_CODE");
            String job= rs.getString("DATA_VALUE");
            System.out.println("ID:"+name+",COED:"+id+",VALUE:"+job);
        }


    }


}
