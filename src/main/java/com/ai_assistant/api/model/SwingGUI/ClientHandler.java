package com.ai_assistant.api.model.SwingGUI;
import com.ai_assistant.api.model.Client;

public interface ClientHandler {
    void setClient(Client client);
    void setLocale(String language, String country);
}
