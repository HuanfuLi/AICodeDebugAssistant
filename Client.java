package main;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String [] args)
    {
        try
        {
            Socket s = new Socket("127.0.0.1", 8189);
            try
            {
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                OutputStream outStream = s.getOutputStream();
                PrintWriter out = new PrintWriter(outStream,true);
                System.out.println("Client Connected to Server.");
                out.println("Where is the capital of France? Please return the answer with an 'EOF' in the end.");
                s.close();
                while(in.hasNextLine())
                {       
                    System.out.println("Client Listen: " + in.nextLine());
                }
            }
            finally
            {
                
            }
        }
        catch(IOException ioexc)
        {
            ioexc.printStackTrace();
        }
} //end public
} //end class

