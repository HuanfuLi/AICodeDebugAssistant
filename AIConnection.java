package main;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIConnection {
    
    public String runConnection(String AIInput, String address){
        String response = "Error occured, please check code.";
        try {
            // Set up the endpoint URL
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON payload
            
            CustomizedJson cJson = new CustomizedJson("deepseek-r1-distill-qwen-7b", "You are a helpful assistant, please answer the question:", AIInput);
            String jsonInputString = cJson.CreateCustomJson();
            /*String jsonInputString = """
            {
              "model": "deepseek-r1-distill-qwen-7b",
              "messages": [
                { "role": "system", "content": "Always answer in rhymes. Today is Thursday" },
                { "role": "user", "content": "What day is it today?" }
              ],
              "temperature": 0.7,
              "max_tokens": -1,
              "stream": false
            }
            """;*/

            // Send the JSON input
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            int status = conn.getResponseCode();
            System.out.println("HTTP Status Code: " + status);

            try (var reader = new java.util.Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                response = reader.useDelimiter("\\A").next();
                System.out.println("Response: " + response);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
