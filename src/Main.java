import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigReader configReader = new ConfigReader();
            int port = configReader.getPort(); 
            String rootDirectory = configReader.getRootDirectory();
            String defaultPage = configReader.getDefaultPage();
            int maxThreads = configReader.getMaxThreads();

            Server server = new Server(port, rootDirectory, defaultPage, maxThreads);
            server.initializeServer();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }
}
