package com.supstore.core.database;

import java.sql.*;
public class DatabaseManager {
    static final String DB_URL = "jdbc:mysql://localhost:3306/istore";
    static final String USER = "root";
    static final String PASS = "98513257";

    public Connection conn = null;
    public Statement st = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;
    public ResultSet usr = null;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            st = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            return st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String sql) {
        try {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void close() {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}