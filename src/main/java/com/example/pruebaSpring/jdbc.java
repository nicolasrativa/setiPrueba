package com.example.pruebaSpring;
import java.sql.Connection;
import java.sql.*;

public class jdbc {

    private static Connection connection = null;
    public static Connection getConnection()throws SQLException {
        if (connection !=null){
            return connection;
        }else {
            String url = "mysql://seti-tech-test.cyaguswpnoyv.us-east-1.rds.amazonaws.com://1433";
            String username = "dev67281";
            String password = "D3vS3t12023$";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from Period");
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return connection;
    }
}
