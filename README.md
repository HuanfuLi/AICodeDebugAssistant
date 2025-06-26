This project is an AI-powered desktop assistant designed to help programmers with code hints, suggestions, and debugging, as well as answer general questions. It features a client-server architecture, a graphical user interface (GUI) built with Swing, and a MySQL database for user management and interaction history.

### Features

* **AI Integration:** Connects to an AI service (e.g., LM Studio) to provide:
    * Code hints
    * Code suggestions
    * Code debugging (generates corrected code with inline comments)
    * Generic question answering
* **Client-Server Architecture:** Separates the AI interaction logic (server) from the user interface (client).
* **User Management:** Allows new user registration and existing user login via a database.
* **History Tracking:** Stores user queries and AI responses in a database, accessible through the GUI's history page.
* **File Content Processing:** Can extract content from local files to be sent as input to the AI service.
* **GUI-based Interaction:** Provides a user-friendly interface for submitting requests, viewing responses, and managing history.
* **Multilingual Support:** The GUI supports multiple languages (English, Simplified Chinese, French) through resource bundles.

### Technologies Used

* **Java:** Core programming language.
* **Swing:** For the graphical user interface.
* **MySQL:** Database for storing user information and interaction history.
* **Gson:** Google's JSON library for handling JSON payloads with the AI service.
* **FlatLaf:** A modern open-source Look and Feel for Swing applications (specifically FlatDarculaLaf is used).

### Setup and Installation

1.  **Database Setup (MySQL):**
    * Ensure MySQL server is running, typically on `localhost:3306`.
    * Create a database named `ai_assistant_database`.
    * The application will automatically create `HISTORY` and `USER` tables when the `Server.java` is run for the first time.
    * The default database credentials are username `root` and password `rootroot`. You might need to adjust these in `DatabaseConnection.java` and `LoginPage.java` if your MySQL setup uses different credentials.

2.  **AI Service:**
    * Set up an AI service compatible with OpenAI API format locally (e.g., LM Studio, Ollama).
    * The `Server.java` is configured to connect to `http://localhost:1234/v1/chat/completions` by default. Ensure your AI service is running at this endpoint.

3.  **Run the Server:**
    * Compile and run the `Server.java` file. This will start the server and initialize the database tables if they don't exist.
    * Example command (after compiling): `java com.ai_assistant.api.model.Server`

4.  **Run the Client (GUI):**
    * Compile and run the `MainFrame.java` file. This will launch the desktop application GUI.
    * Example command (after compiling): `java com.ai_assistant.api.model.SwingGUI.MainFrame`

### Usage

1.  **Login/Register:** Upon launching the GUI, you will be presented with a login page. You can register a new account or log in with existing credentials.
2.  **Main Panel:** After successful login, the main application panel will appear.
3.  **Page 1:** Allows you to input a file path to extract code content and send it to the AI for analysis (Hint, Suggestion, Debug). You can also overwrite the original file with the AI's response.
4.  **Page 2:** Provides a text area for direct input to the AI and displays the AI's response in a separate output area.
5.  **History Page:** View your past interactions with the AI, filter by function type, and resend previous queries.
6.  **Locale Selection:** Change the GUI language from the login page.
7.  **Menu Options:** Access "About" information or "Exit" the application from the menu bar.
