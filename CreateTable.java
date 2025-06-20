package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable extends DatabaseConnection implements DatabaseHandler{
    Connection conn = null;
    Statement statement = null;
    
    @Override
    public String connectDatabase(Prompt prompt) {
        try {
            //Establish connection to database
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("<DATABASE> Connected to database for table creation.");

            if (conn != null) {
                statement = conn.createStatement();

                String createTableSQL = "CREATE TABLE IF NOT EXISTS HISTORY (" +
                                        "RECORD_ID INT AUTO_INCREMENT PRIMARY KEY," +
                                        "UID INT NOT NULL," +
                                        "SELECTION VARCHAR(10) NOT NULL,"+
                                        "CONTENT VARCHAR(4096)," +
                                        "RESPONSE VARCHAR(4096)," +
                                        "TIME_STAMP DATETIME" +
                                        ")";
                statement.executeUpdate(createTableSQL);
                System.out.println("<DATABASE> HISTORY table created or already exists.");

                String createTableSQL2 = "CREATE TABLE IF NOT EXISTS USER (" +
                                        "UID INT AUTO_INCREMENT PRIMARY KEY," +
                                        "EMAIL VARCHAR(20) NOT NULL,"+
                                        "PASSWORD VARCHAR(20) NOT NULL" +
                                        ")";
                statement.executeUpdate(createTableSQL2);
                System.out.println("<DATABASE> USER table created or already exists.");
            }
        } catch (SQLException e) {
            System.err.println("<DATABASE> Error: Cannot create table: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, statement, null);
        }
        return null;
    }



    // Test main
    // public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    //     // Class.forName("com.mysql.jdbc.Driver").newInstance();
    //     Prompt test = new Prompt(1, 1, null, null);
    //     test.setTimeStamp("25-04-21 03:26:15");
    //     CreateTable table = new CreateTable();
    //     table.connectDatabase(test);
    // }
}
