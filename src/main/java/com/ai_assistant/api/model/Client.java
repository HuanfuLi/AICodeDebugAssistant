package com.ai_assistant.api.model;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements ConnectionHandler
{
    //Client ID is from the UID extracted from database upon authentication, passed to all classes for consistent functionality
    int clientID;

    public Client(int clientID){
        this.clientID = clientID;
    }

    //Getter
    public int getUID(){
        return clientID;
    }
    //Setter
    public void setUID(int UID){
        this.clientID = UID;
    }
        
    //Note: The following connection takes port and address of server
    @Override
    public String runConnection(int port, String address, Prompt passedInfo){
        StringBuilder sb = new StringBuilder();
        try
        {
            Socket s = new Socket(address, port);
            try
            {
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                OutputStream outStream = s.getOutputStream();
                PrintWriter out = new PrintWriter(outStream,true);
                System.out.println("Client Connected to Server. Passed question: " + passedInfo.toString());
                out.println(passedInfo.toString());
                out.println("END_OF_MESSAGE");//Use EOF signal to stop the server from waiting
                sb = new StringBuilder();
                while (in.hasNextLine())
                {       
                    sb.append(in.nextLine());
                }
            }
            finally
            {
                s.close();
                //in.close();
            }
        }
        catch(IOException ioexc)
        {
            ioexc.printStackTrace();
        }
        return sb.toString();
    }

    //Test main
    public static void main(String [] args)
    {
        Client c1 = new Client(0);

        //Debug 1: Work with file extractor class to extract content and path to form a prompt. Below are code:
        // FileExtractor file1 = new FileExtractor("/Users/huanfuli/CNIT325FinalProject/api/src/main/java/com/ai_assistant/api/model/Testcode.java");
        // String question = file1.getContent();

        //Debug 2: Simple question (less process time for faster debugging)
        String question = "When was the USA founded?";

        Prompt prompt = new Prompt(0, 3, question, null); 
        //Selection 0:Code Hint || 1:Code Suggestion || 2: Code Debug || 3:Generic
        
        String result = c1.runConnection(8189, "127.0.0.1", prompt);
        System.out.println(result);
    } //end public
} //end class

