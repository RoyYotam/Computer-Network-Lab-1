The project is submitted by: Roy Yotam (206378838) and Itay Tiomkin (207828914)

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

(4) HttpResponse - Handles the logic of Http responses.
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

(8) ConnectionHandler - a nested class, written in the file Server.java.
    Handles the logic of the connection between the server and its clients (max. 10).
    The class implements the interface 'Runnable' - allowing its instances to be executed as threads.


An explanation about the design of the server implementation:

We chose to implement the server using 7 java files, featuring overall 8 classes.
The server itself is located in the Server.java file, while the file Main.java uses to build the server -
based the values written in the file config.ini - and initialize it, given that the values in config.ini are valid.
Http requests are being handled by 2 files:
1. RequestHandler.java - interacts with the server file, getting Http requests from it and returning Http responses.
2. HttpRequest.java - recieveing unhandled requests from RequestHandler.java and returning them handled. 
   Http responses are being handled by 2 files:
1. HttpResponse.java - Processes an Http request and builds a Http response object.
2. HttpResponseHelper - Generates an Http response using the Http response object, 
   corresponding to the Http request processed by HttpResponse.java, and transmits it to RequestHandler.java.
The file ConfigReader.java is used to parse and process the values written config.ini, for the Main.java file to use them
while building the server - as well as for the RequestHandler.java file to use them while handling client's requests.

A clear demonstration of the intercation between the server classes can be found in the attached "Server_Chart.png" file.
