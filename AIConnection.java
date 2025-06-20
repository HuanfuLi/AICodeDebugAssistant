package com.ai_assistant.api.model;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIConnection {

    //Helper function to select system prompts for AI input based on user selection
    public String getSelection(int selectionInt){
        switch (selectionInt) {
        //Indexes of functions: 0. Hint 1. Suggestion 2. Debug 3. Generic
        case 0:
        return "You are an expert programming advisor. " +
                "When a programmer provides a snippet of code, your ONLY task is to analyze it for potential issues, areas for improvement, and adherence to best practices. " +
                "You MUST provide your feedback and suggestions exclusively in detailed natural language explanations. " +
                "Under NO circumstances should you generate or include any executable code in your response. " +
                "Your goal is to guide the programmer with insightful advice and explanations, not to provide code solutions.";
                //Debug: Test if AI recognizes prompt
                // "Please append an 'HINT' signal at the end of your response.";
    case 1:
        return "You are a helpful and experienced assistant in programming." +
                "Here is a snippet of code from a programmer who needs your help." +
                "Please provide suggestions, identify issues, and propose possible "+
                "solutions in a bulleted list format.";
                // "Please append an 'SUGGESTION' signal at the end of your response.";
    case 2:
        return "You are a helpful and experienced assistant in programming. " +
                "Generate a corrected and improved version of the code provided. " +
                "Please put all your explanations in between the code as comments.";
                // "Any explanations or comments should be included directly within the code as comments."+
    case 3:
        return "You are a helpful assistant on daily routine topics. "+
                "Please answer questions from the user in a short paragraph.";
                // "Please append an 'GENERIC' signal at the end of your response.";
            default:
                throw new AssertionError();
        }
    }

    public String runConnection(int selection, String AIInput, String address){
        String response = "Error occured, please check code.";
        String result = "Error: Cannot generate response.";

        try {
            // Set up the endpoint URL
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set request method and headers following LM Studio format
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create JSON payload. Format: model, system content, user content.
            CustomizedJson cJson = new CustomizedJson("deepseek-r1-distill-qwen-7b", getSelection(selection), AIInput);
            String jsonInputString = cJson.CreateCustomJson();

            // Send the JSON input
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            int status = conn.getResponseCode();
            System.out.println("<AI SERVICE> HTTP Status Code: " + status);

            try (var reader = new java.util.Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                response = reader.useDelimiter("\\A").next();
                System.out.println("<AI SERVICE> Response: " + response);
            }
            //Process AI output so that it only contains useful content
            //If user selects Debug function (selection2), trim response so that it only contains executable code
            if (selection == 2){
                String[] parts = response.split("```");
                String tempResult = parts[1];
                result = "//" + tempResult; //Comment out language name (e.g.: java) in AI response
            }else{
                String[] parts = response.split("</think>");
                String tempResult = parts[1];
                String[] parts1 = tempResult.split("prompt_tokens");
                result = parts1[0].trim();
                result = result.substring(0, result.length()-35);  //Truncate the later part of result
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}