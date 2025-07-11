package com.ai_assistant.api.model;

public interface ConnectionHandler {
    String runConnection(int port, String address, Prompt passedInfo);
}
