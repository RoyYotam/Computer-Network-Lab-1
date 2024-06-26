import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                // Create a new thread to handle the client request

                Runnable listener = new ConnectionHandler(clientSocket);
                executor.execute(listener);
            }
        }
        catch (IOException e)
        {
            System.out.println("Some error accorded in server!");
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
                String request = recvWholePacket();
                byte[][] response = RequestHandler.HandleClientRequest(request);
                sendWholePacket(response);
            } catch (Exception e) {
                // closed
                this.IsAlive = false;
            }
        }
    }

    private String recvWholePacket() throws IOException {
        final int recvLength = 4096;

        char[] messageChars = new char[recvLength];
        int totalCharsRead = reader.read(messageChars);

        return new String(messageChars, 0, totalCharsRead);
    }

    private void sendWholePacket(byte[][] packet) throws IOException {
        for (byte[] packetChunk: packet)
        {
            outputStream.write(packetChunk);
        }
    }
}