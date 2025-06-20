package com.ai_assistant.api.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileExtractor {
    String filePath;
    String fileContent;
    String extension;

    public FileExtractor(String filePath){
        this.filePath = filePath;
        this.fileContent = extractContent(filePath);
        this.extension = extractExtension(filePath);
    }

    //Getters
    public String getFilePath(){
        return filePath;
    }
    public String getContent(){
        return fileContent;
    }
    public String getExtension(){
        return extension;
    }

    //Setters
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public void setContent(String content){
        this.fileContent = content;
    }
    public void setExtension(String extension){
        this.extension = extension;
    }

    //Extract the file extension for later rewriting functionality
    public final String extractExtension(String filePath){
        int lastIndexOfDot = filePath.lastIndexOf(".");
        if (lastIndexOfDot == -1){
            return "No extension";
        }
        return filePath.substring(lastIndexOfDot + 1);
    }

    //
    public final String extractContent(String filePath){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            FileReader fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) { 
                sb.append(line).append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            System.err.println("Error: Cannot read the file, please check file path and code: " + e.getMessage());
            return null;
        }finally{
            try {
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error: Cannot close bufferedReader: " + e.getMessage());
            }
        }
    }
}
