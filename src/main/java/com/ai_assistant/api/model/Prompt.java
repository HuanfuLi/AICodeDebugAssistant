package com.ai_assistant.api.model;

public class Prompt{
    int UID;
    int selection;
    String content;
    String response;
    String timeStamp;

    public Prompt(int UID, int selection, String content, String response){
        this.UID = UID;
        this.selection = selection;
        this.content = content;
        this.response = response;
        timeStamp = null;
    }

    //Getters
    public int getUID(){
        return UID;
    }

    public int getSelection(){
        return selection;
    }

    public String getContent(){
        return content;
    }

    public String getResponse(){
        return response;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    //Setters
    public void setUID(int UID){
        this.UID = UID;
    }

    public void setSelection(int selection){
        this.selection = selection;
    }
    public void setContent(String content){
        this.content = content;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }

    //Overriden toString method for serialization, use the delimiter as "<DELIMITER>""
    @Override
    public String toString(){
        return UID + "<DELIMITER>" + selection + "<DELIMITER>" + content + "<DELIMITER>" + response + "<DELIMITER>" + timeStamp;
        //Serialized format: UID<DELIMITER>selection<DELIMITER>content<DELIMITER>response<DELIMITER>timeStamp
    }
}