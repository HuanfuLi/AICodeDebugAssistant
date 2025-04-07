package main;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server{
    private int port;
    private String address;

    public Server(int port, String address){
        this.port = port;
        this.address = address;
    }

    public void setServerPort(int port){
        this.port = port;
    }
    public void setIPAddress(String address){
        this.address = address;
    }

    public int getServerPort(){
        return port;
    }
    public String getIPAddress(){
        return address;
    }

    public void runConnection(int port, String address){
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server: System up and running.");
            boolean over = false;
            while (!over){
                Socket incoming = ss.accept();
                System.out.println("Client Connected.");
                try{
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStreamToClient = incoming.getOutputStream();
                    Scanner input = new Scanner(inStream);
                    PrintWriter output = new PrintWriter(outStreamToClient, true);

                    StringBuilder AIInput = new StringBuilder();
                    System.out.println("Next into while loop");
                    while (input.hasNextLine()){
                        String thisLine = input.nextLine();
                        AIInput.append(thisLine);
                    }
                    System.out.println("Out of while loop");
                    System.out.println(AIInput.toString());
                    AIConnection toAI = new AIConnection();
                    String response = toAI.runConnection(AIInput.toString(), address);

                    //以下是返回给客户端的信息
                    output.println(response);
                    System.out.println("Transaction completed.");
                }
                finally{
                    incoming.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {
        Server s1 = new Server(8189, "http://localhost:1234/v1/chat/completions");
        s1.runConnection(8189, "http://localhost:1234/v1/chat/completions");
    }

}

