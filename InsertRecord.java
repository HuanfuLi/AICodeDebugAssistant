package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertRecord extends DatabaseConnection implements DatabaseHandler{
    Connection conn = null;
    PreparedStatement statement = null;
    
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
                System.out.println("<DATABASE> Connected to database for record insertion.");

                //Use prepared statment rather than normal statement to prevent SQL injection.
                String insertRecordSQL = "INSERT INTO HISTORY (UID, SELECTION, CONTENT, RESPONSE, TIME_STAMP) " +
                                        "VALUES (?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(insertRecordSQL);
                statement.setInt(1, prompt.getUID());
                statement.setString(2, DatabaseConnection.getSelection(prompt.getSelection()));
                statement.setString(3, prompt.getContent());
                statement.setString(4, prompt.getResponse());
                statement.setString(5, prompt.getTimeStamp());

                //Integrity check based on rows affected in database
                int rowsAffected = statement.executeUpdate();
                System.out.println("<DATABASE> SQL executed.");
                if (rowsAffected == 0){
                    System.out.println("<DATABASE> Error: Insertion failed.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Insertion failed: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(conn, statement, null);
        }
        return null;
    }
    
    //Test main
    // public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    //     // Class.forName("com.mysql.jdbc.Driver").newInstance();
    //     Prompt test = new Prompt(1, 1, "test con", "test response");
    //     test.setTimeStamp("25-04-21 03:26:15");
    //     InsertRecord record = new InsertRecord();
    //     record.connectDatabase(test);
    // }


}
