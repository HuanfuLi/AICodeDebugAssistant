package com.ai_assistant.api.model;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    //Note: The port is referred to Server Port for this Server class, but the Address is referred to URL of AI service
    public void runConnection(int port, String address){
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("<SERVER> Server up and running.");
            boolean over = false;
            while (!over){
                Socket incoming = ss.accept();
                System.out.println("<SERVER> Client Connected.");
                try{
                    InputStream inStream = incoming.getInputStream();
                    OutputStream outStreamToClient = incoming.getOutputStream();
                    BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
                    PrintWriter output = new PrintWriter(outStreamToClient, true);

                    // String inputStr = input.nextLine();
                    // Prompt prompt = parsePrompt(inputStr);

                    StringBuilder AIInput = new StringBuilder();
                    String line;
                    while ((line = input.readLine()) != null){
                        if (line.trim().equals("END_OF_MESSAGE")){
                            break;
                        }
                        AIInput.append(line).append("\n"); // Append line with newline
                    }
                    Prompt prompt = parsePrompt(AIInput.toString());
                    AIConnection toAI = new AIConnection();
                    
                    //Send prompt content to AI and set the response of prompt with returned value
                    String response = toAI.runConnection(prompt.selection, prompt.content, address);
                    prompt.setResponse(response);

                    //Implement JDBC below, use prompt object UID and timeStamp to store question and response to database
                    InsertRecord record = new InsertRecord();
                    record.connectDatabase(prompt);


                    //Return response String to client, then client displays String directly. Client does not need to access prompt objects.
                    output.println(response);
                    // System.out.println(prompt.getTimeStamp());
                    System.out.println("<SERVER> Task completed.");
                }
                finally{
                    incoming.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    //Helper function to parse serialized Prompt object
    //Serialized format: UID<DELIMITER>selection<DELIMITER>content<DELIMITER>response<DELIMITER>timeStamp
    public Prompt parsePrompt(String str){
        String[] parts = str.split("<DELIMITER>");
        int UID = Integer.parseInt(parts[0]);
        int selection = Integer.parseInt(parts[1]);
        Prompt prompt = new Prompt(UID, selection, parts[2], parts[3]);
        prompt.setTimeStamp(getTimeStamp());
        return prompt;
    }

    //Helper function to get timestamp from time server
    //Output format: 25-04-21 03:26:15
    public String getTimeStamp(){
        String timeStamp = "<SERVER> Time Stamp Not Available";
        String tempTimeStamp = "";
        String mysqlDateTimeString = "";
        try{
            Socket s = new Socket("time-A.timefreq.bldrdoc.gov", 13);
            try{
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                
                while(in.hasNextLine()){
                    tempTimeStamp = in.nextLine();
                }
                //Split the output from time server and trim it until it only contain time information
                String[] parts = tempTimeStamp.split(" ");
                if (parts.length >= 3){
                    timeStamp = parts[1] + " " + parts[2];
                }
                //Parse the output time string into MySQL DATETIME format of yyyy-MM-dd HH:mm:ss
                DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(timeStamp, inputFormat);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                mysqlDateTimeString = localDateTime.format(outputFormatter);
            }
            finally{
                s.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return mysqlDateTimeString;
    }

    public static void main(String[] args) {
        //Check and create the HISTORY table before server start
        CreateTable table = new CreateTable();
        table.connectDatabase(new Prompt(0, 0, null, null));

        //Set the AI service URL here
        Server s1 = new Server(8189, "http://localhost:1234/v1/chat/completions");
        s1.runConnection(8189, "http://localhost:1234/v1/chat/completions");
    }
}