import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigReader configReader = new ConfigReader();

            // Correctly retrieve configuration values with the appropriate data types
            int port = Integer.parseInt(configReader.get("port"));
            String rootDirectory = configReader.get("root");
            String defaultPage = configReader.get("defaultPage");
            int maxThreads = Integer.parseInt(configReader.get("maxThreads"));

            // Initialize and start the server
            Server server = new Server(port, rootDirectory, defaultPage, maxThreads);
            server.startServer();
        } catch (IOException e) {
            // Gracefully handle IOException
            ExceptionHandler.handleException(e);
        } catch (NumberFormatException e) {
            // Handle the case where parsing integers fails
            ExceptionHandler.handleException(e);
        }
    }
}
