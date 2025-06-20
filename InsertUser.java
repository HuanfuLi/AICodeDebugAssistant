package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertUser extends DatabaseConnection implements DatabaseHandler{
    Connection conn = null;
    PreparedStatement statement = null;
    private String Email;
    private String Password;

    public InsertUser (String email, String password){
        this.Email = email;
        this.Password = password;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public void setStatement(PreparedStatement statement) {
        this.statement = statement;
    }


    @Override
    public String connectDatabase(Prompt prompt) {
        try {
            //Establish connection to database
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            if (conn != null) {
                System.out.println("<DATABASE> Connected to database for user registration.");

                String insertRecordSQL = "INSERT INTO USER (EMAIL, PASSWORD) " +
                                        "VALUES (?, ?)";
                statement = conn.prepareStatement(insertRecordSQL);
                statement.setString(1,this.Email);
                statement.setString(2,this.Password);


                int rowsAffected = statement.executeUpdate();
                System.out.println("<DATABASE> SQL executed.");

                if (rowsAffected == 0){
                    System.out.println("<DATABASE> Error: Insertion failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("<DATABASE> Error: Insertion failed: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, statement, null);
        }
        return null;
    }
    
}
