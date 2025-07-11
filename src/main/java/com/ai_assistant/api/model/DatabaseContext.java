package com.ai_assistant.api.model;

public class DatabaseContext {
    private DatabaseHandler strategy;

    public void setHandler(DatabaseHandler strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(Prompt prompt){
        if (strategy == null){
            System.err.println("<SERVER ERROR> Database Strategy not set.");
            return;
        }
        else{
            strategy.execute(prompt);
        }
    }
}
