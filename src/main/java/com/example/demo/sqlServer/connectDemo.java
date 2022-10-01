package com.example.demo.sqlServer;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

/**
 * 功能描述
 *
 * @author: 杜莉莎
 * @date: 2022年09月20日 15:51
 */
public class connectDemo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String temp = "222";
        String url = "jdbc:sqlserver://localhost:1434;DatabaseName=pushDB";
        String user = "sa";
        String password = "20001226";
        ResultSet rs = null;
        String sqlServer_tableName = "pushTest";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("连接成功1");
        } catch (Exception e) {
            System.out.println("连接失败1");
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("连接成功2");
            String sql = "insert into " + sqlServer_tableName  + " (" + FIELD_ID + ","
                    + FIELD_DATA_VALUE + "," + FIELD_DATA_TYPE + "," + FIELD_DATA_CODE + "," + FIELD_CONNECTED  + "," + FIELD_UPDATE_TIME +
                    " )" + " values(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, UUID.randomUUID().toString());
            preparedStatement.setDouble(2, Double.parseDouble(temp));
            preparedStatement.setString(3, temp);
            preparedStatement.setString(4, temp);
            preparedStatement.setBoolean(5, true);
            preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));

            int result = preparedStatement.executeUpdate();
            System.out.println(result);
            connection.close();
        } catch (SQLException e) {
            System.out.println("请输入正确的表名" + e);
            System.out.println("连接失败2");
        }


    }

    private final static String FIELD_ID = "ID";
    private final static String FIELD_DATA_CODE = "DAP_CODE";
    private final static String FIELD_DATA_TYPE = "DAP_TYPE_CODE";
    private final static String FIELD_DATA_VALUE = "DATA_VALUE";
    private final static String FIELD_CONNECTED = "Enable";
    private final static String FIELD_EQP_IDENTITY = "EQP_IDENTITY";
    private final static String FIELD_UPDATE_TIME = "UPDATE_TIME";


}
