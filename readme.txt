A list of the classes implemented building the server:

(1) Main - The entry point of the application (server). 
    Uses the values read from config.ini to start the server.
    Handles exceptions, if needed.

(2) Server - Handles the logic of the server.
    Contains the methods used to initialize the server.
    Sets the server to listen to the required port (8080).
    Responsible of the server's communication with its clients (using threads).

(3) HTTPResponse - The core class of the server.
    Handles the logic of processing, handling and responding HTTP requests

(4) ConfigReader - Reads and parses config.ini.
    Converts the file's data into a data structure, used to retrieve them for the server's initialization.


An explanation about the design of the server implementation:

-