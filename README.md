# AI Code Debug Assistant

This project is a desktop application built with Java that serves as an AI-powered assistant for debugging and improving code. It provides a graphical user interface (GUI) for users to interact with an AI model to get hints, suggestions, and corrections for their code snippets.

## Features

  * **User Authentication:** Users can create an account and log in to the application.
  * **Multiple Inquiry Methods:**
      * **Paste Inquiry:** Directly paste code or questions into an input text area.
      * **File Inquiry:** Provide a file path to have the application read the content and send it for analysis.
  * **Diverse AI Functions:**
      * **Hint:** Get high-level advice and best practice recommendations.
      * **Suggestion:** Receive a bulleted list of potential issues and improvements.
      * **Debug:** Get a corrected and improved version of the code with inline comments.
      * **Generic:** Ask any other question.
  * **Inquiry History:** View a log of all past inquiries, with the ability to filter by function.
  * **Detailed History View:** Double-click on a history entry to see the full request and response, and even resend the request.
  * **File Overwrite:** Option to overwrite the original file with the AI-generated code.
  * **Internationalization:** The user interface supports multiple languages, including English, French, and Chinese.

## Technologies Used

  * **Backend:** Java, Spring Boot
  * **Frontend (GUI):** Java Swing, FlatLaf (for look and feel)
  * **Database:** MySQL
  * **AI Integration:** Connects to a local AI model endpoint (e.g., LM Studio).
  * **Dependencies:**
      * `spring-boot-starter-web`: For web server capabilities.
      * `mysql-connector-j`: For connecting to the MySQL database.
      * `gson`: For creating and parsing JSON data for AI requests.
      * `flatlaf`: For modern Swing UI themes.

## Setup and Installation

### Prerequisites

  * **Java Development Kit (JDK):** Version 21 or higher.
  * **Maven:** For managing project dependencies.
  * **MySQL:** A running MySQL server instance.
  * **AI Service:** A local AI model server that is compatible with the OpenAI chat completions API format (e.g., LM Studio).

### 1\. Database Setup

1.  Make sure your MySQL server is running.
2.  Create a new database named `ai_assistant_database`.
3.  The application will automatically create the necessary `HISTORY` and `USER` tables when the server starts for the first time.

### 2\. Configuration

1.  **Database Connection:** Open `src/main/java/com/ai_assistant/api/model/DatabaseConnection.java` and update the `JDBC_URL`, `USERNAME`, and `PASSWORD` fields with your MySQL credentials.

2.  **AI Service URL:** Open `src/main/java/com/ai_assistant/api/model/Server.java` and in the `main` method, update the URL for the AI service if it's different from the default `http://localhost:1234/v1/chat/completions`.

### 3\. Build and Run

1.  **Run the Server:**

      * Open a terminal and navigate to the project root directory.
      * Execute the `Server` class located at `src/main/java/com/ai_assistant/api/model/Server.java`. This will start the backend server that listens for client connections.

2.  **Run the Client Application:**

      * In a new terminal, execute the `MainFrame` class located at `src/main/java/com/ai_assistant/api/model/SwingGUI/MainFrame.java`. This will launch the Swing GUI.

## How to Use

1.  **Login/Register:**

      * On the login screen, enter your credentials or click "Register" to create a new account.
      * You can select your preferred language from the dropdown menu.

2.  **Navigate the Interface:**

      * After logging in, you'll see a menu on the left side to navigate between different pages: "Path Inquiry," "Paste Inquiry," and "Inquiry History."

3.  **Making an Inquiry:**

      * **Path Inquiry:**
          * Go to the "Path Inquiry" page.
          * Enter the full path to your code file in the text field.
          * Select the desired function (Hint, Suggestion, etc.) from the dropdown.
          * Click "Send" to get the AI's response.
          * The response will be displayed in the text area. You can edit it and then click "Overwrite" to save the changes back to the original file.
      * **Paste Inquiry:**
          * Go to the "Paste Inquiry" page.
          * Paste your code or question into the "Inquiry" text area.
          * Select the function and click "Send".
          * The AI's response will appear in the "Answer" text area.

4.  **Viewing History:**

      * Go to the "Inquiry History" page.
      * The table displays your past inquiries.
      * Use the filter dropdown to view inquiries of a specific type.
      * Double-click any row to open a detailed view where you can see the full text and resend the inquiry.

## Project Structure

```
.
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── ai_assistant
        │           └── api
        │               ├── model
        │               │   ├── SwingGUI  # All Java Swing UI classes
        │               │   ├── AIConnection.java   # Handles connection to the AI service
        │               │   ├── Client.java         # Client-side connection logic
        │               │   ├── Server.java         # Server-side connection logic
        │               │   ├── Database*.java    # Classes for database interaction
        │               │   └── ...
        │               └── ApiApplication.java # Spring Boot entry point
        └── resources
            ├── LocaleBundle_*.properties # Language files for internationalization
            └── application.properties
```
