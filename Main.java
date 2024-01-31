import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(ConfigReader.Get("port"));
            int maxThreads = Integer.parseInt(ConfigReader.Get("maxThreads"));

            Server.InitializeAndStartServer(port, maxThreads);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
    }
}