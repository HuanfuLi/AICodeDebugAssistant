package com.ai_assistant.api.model;

import java.sql.Connection;
import java.sql.Statement;

public interface DatabaseHandler {
        Connection conn = null;
        Statement statement = null;
    String connectDatabase(Prompt prompt);
}
