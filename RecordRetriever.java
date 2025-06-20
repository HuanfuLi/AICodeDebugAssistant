package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class RecordRetriever extends DatabaseConnection{
    Connection conn = null;
    Statement statement = null;
    int selectedFunction;

    public RecordRetriever(){
        //No parameters needed, use default
    }
    
    public ResultSet getTable(int UID, int selectedFunction) {
        ResultSet resultSet = null;
        try {
            //Establish connection to database
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            if (conn != null) {
                System.out.println("<DATABASE> Connected to database for record retrieval.");
                statement = (Statement) conn.createStatement();

                String getTableSQL = "SELECT * FROM HISTORY WHERE UID = " +  UID;

                //Add SQL constraint if selection filter is set. Note: selection 5 is "All" in filter comboBox
                if (selectedFunction != 5){
                    String function = DatabaseConnection.getSelection(selectedFunction);
                    getTableSQL = getTableSQL + " AND SELECTION = '" + function + "'";
                }

                //Add SQL query constraint if time filter is set
                //To-do code here

                resultSet = statement.executeQuery(getTableSQL);

                return resultSet;
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    //Test main
   public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        RecordRetriever retriever = new RecordRetriever();
        ResultSet rs = retriever.getTable(1, 5);

        try {
            if (rs != null) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Print column names
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + "\t");
                }
                System.out.println();

                // Print the data
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(rs.getString(i) + "\t");
                    }
                    System.out.println();
                }
                rs.close();
            } else {
                System.out.println("<DATABASE TEST> Result set is null.  Check your database connection and query.");
            }
        } catch (SQLException e) {
            System.err.println("<DATABASE TEST> Error processing result set: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in a finally block
            try {
                if (retriever.statement != null) {
                    retriever.statement.close();
                }
                if (retriever.conn != null) {
                    retriever.conn.close();
                }
            } catch (SQLException e) {
                System.err.println("<DATABASE TEST> Error closing resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
   
}
