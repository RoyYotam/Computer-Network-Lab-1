import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final int maxThreads;

    private Server(int port, int maxThreads) {
        this.port = port;
        this.maxThreads = maxThreads;
    }

    public static void InitializeAndStartServer(int port, int maxThreads) {
        Server server = new Server(port, maxThreads);
        server.startServer();
    }

     public void startServer() {
         try {
             ServerSocket serverSocket = new ServerSocket(port);
             System.out.println("Server listening on port " + port);

             while (true)
             {
                 Socket clientSocket = serverSocket.accept();
                 // Create a new thread to handle the client request
                 Thread thread = new Thread(new ConnectionHandler(clientSocket));
                 // TODO::: THREADPOOL as shown in class
                 thread.start();
             }
         }
         catch (IOException e)
         {
             System.out.println("Something went wrong!");
         }
    }
}

class ConnectionHandler implements Runnable {
    private BufferedReader reader;
    public DataOutputStream outputStream;
    public boolean IsAlive;

    ConnectionHandler(Socket connectionSocket) {
        this.IsAlive = true;

        try {
            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outputStream = new DataOutputStream(connectionSocket.getOutputStream());
        } catch (IOException e) {
            IsAlive = false;
        }
    }

    @Override
    public void run() {

        while(this.IsAlive) {
            try {
                String request = reader.readLine();
                outputStream.write(RequestHandler.HandleClientRequest(request));
            } catch (Exception e) {
                // closed
                this.IsAlive = false;
            }
        }
    }
}
