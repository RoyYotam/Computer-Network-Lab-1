import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final String rootDirectory;
    private final String defaultPage;
    private final int maxThreads;

    public Server(int port, String rootDirectory, String defaultPage, int maxThreads) {
        this.port = port;
        this.rootDirectory = rootDirectory;
        this.defaultPage = defaultPage;
        this.maxThreads = maxThreads;
    }

    public void initializeServer() throws IOException {
        Server server = new Server(port, rootDirectory, defaultPage, maxThreads);
        server.startServer();
    }

     public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            // Create a new thread to handle the client request
            Thread thread = new Thread(new RequestHandler(clientSocket, rootDirectory, defaultPage));
            thread.start();
        }
    }
}
