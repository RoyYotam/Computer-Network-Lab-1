The project is submitted by: Roy Yotam (id) and Itay Tiomkin (207828914)

A list of the classes implemented building the server:

(1) Main - The entry point of the application (server). 
    Uses the values read from config.ini to build a server object and start a server.
    Handles exceptions related to the file config.ini, if needed.

(2) Server - Handles the logic of the server.
    Contains the methods used to initialize the server.
    Sets the server to listen to the required port (8080).
    Responsible of the server's communication with its clients (using threads)
    Limits the number of connected clients to 10 (maxThreads) using a thread pool.
    Opens a new handler (of the nested ConnectionHandler class) for each client, receiving information from the client.

(3) ConfigReader - Reads and parses config.ini.
    Converts the file's data into a data structure, used to retrieve them for the server's initialization.

(4) HTTPResponse - Handle the logic of Http responses.
    Responsible for returning Http responses when needed.
    Utilizes the seperate HttpResponseHelper class.

(5) HttpResponseHelper - Generates Http responses for the HTTPResponse class to return.
    Seperates between several cases (and codes) of Http responses:
    200 (Ok) / 404 (Not found) / 501 (Not implemented) / 400 (Bad request) / 500 (Internal server error)

(6) RequestHandler - Handles the logic of Http requests.
    Generates Http responses to the handled requests.
    Prints Http requests and responses when needed. 
    Utilizes the seperate HttpRequest class.

(7) HttpRequest - Parses the requests managed by RequestHandler.
    Goes over a given request - checks for the validity of the request's parameters.
    Handles exceptions related to Http requests, if needed (by creating corresponding Http responses).
    Verifies that a requested page path is valid, and that requests methods are being supported.
    Dictates the supported image files types (".bmp", ".gif", ".png", ".jpg").


An explanation about the design of the server implementation:

-
