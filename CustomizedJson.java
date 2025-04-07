package main;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CustomizedJson {
    String model;
    String sysContent;
    String userContent;

    //Constructor
    public CustomizedJson(String model, String sysContent, String userContent){
        this.model = model;
        this.sysContent = sysContent;
        this.userContent = userContent;
    }

    //Setter methods
    public void setModel(String model){
        this.model = model;
    }
    public void setSysContent(String sysContent){
        this.sysContent = sysContent;
    }
    public void setUserContent(String userContent){
        this.userContent = userContent;
    }

    //Getter methods
    public String getModel(){
        return model;
    }
    public String getSysContent(){
        return sysContent;
    }
    public String getUserContent(){
        return userContent;
    }

    public String CreateCustomJson(){
        Gson gson = new Gson();

        JsonObject input= new JsonObject();
        input.addProperty("model", model);

        JsonArray messagesArray = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", sysContent);
        messagesArray.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userContent);
        messagesArray.add(userMessage);

        input.add("messages",messagesArray);
        input.addProperty("temperature", 0.7);
        input.addProperty("max_tokens", -1);
        input.addProperty("stream", false);

        return gson.toJson(input);
    }

    /*public static void main(String[] args) {
        String testCode = "Some Test Code Here!";
        CustomizedJson debug = new CustomizedJson("deepseek-r1-distill-qwen-7b", "You are a helpful assistant, please answer questions from user", testCode);
        String input = debug.CreateCustomJson();
        System.out.println(input);
    }*/
}
