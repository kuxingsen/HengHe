package com.henghe.utils;

import java.sql.*;
import com.henghe.bean.Content.*;

import static com.henghe.bean.Content.*;

public class DbUtil{

    private static Statement s = null;
    private static ResultSet rs = null;
    private static Connection conn = null;

    private static synchronized Connection getConnection() {
        if(conn == null){
            try {
                Class.forName(DRIVER);
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static int executeUpdate(String sql) {
        int result = 0;
        try {
            s = getConnection().createStatement();
            result = s.executeUpdate(sql);
        } catch(SQLException e) {

            e.printStackTrace();
        }
        return result;
    }

    public static ResultSet executeQuery(String sql) {

        try {
            s = getConnection().createStatement();
            rs = s.executeQuery(sql);
        } catch(SQLException e) {

            e.printStackTrace();
        }
        return rs;
    }

    public static PreparedStatement executePreparedStatement(String sql) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ps;
    }

    public static void rollback() {
        try {
            getConnection().rollback();
        } catch(SQLException e) {

            e.printStackTrace();
        }

    }

    public static void close() {
        try {
            if(rs != null)
                rs.close();
            if(s != null)
                s.close();
            if(conn != null)
                conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
