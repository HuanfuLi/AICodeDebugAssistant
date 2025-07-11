package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseConnection {
    //Database connection basic parameters
    protected  static final String JDBC_URL = "jdbc:mysql://localhost:3306/ai_assistant_database";
    protected  static final String USERNAME = "root";
    protected  static final String PASSWORD = "rootroot";

    //Close resource utilities used after each DB operation
    protected static void closeResources(Connection conn, Statement stmt, java.sql.ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("<DATABASE> Error closing ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("<DATABASE> Error closing Statement: " + e.getMessage());
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("<DATABASE> Error closing Connection: " + e.getMessage());
        }
    }

    public static String getSelection(int selection){
        switch (selection){
            case 0:
                return "Hint";
            case 1:
                return "Suggestion";
            case 2:
                return "Debug";
            case 3:
                return "Generic";
            default:
                throw new IllegalArgumentException("<DATABASE> Invalid selection value: " + selection);
        }
    }
}
