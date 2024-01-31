import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigReader configReader = new ConfigReader();
            int port = configReader.get(port); 
            String rootDirectory = configReader.get(root);
            String defaultPage = configReader.get(defaultPage);
            int maxThreads = configReader.get(maxThreads);

            Server server = new Server(port, rootDirectory, defaultPage, maxThreads);
            server.initializeServer();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
