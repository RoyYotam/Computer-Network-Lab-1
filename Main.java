public class Main {
    public static void main(String[] args) {
        try
        {
            int port = Integer.parseInt(ConfigReader.Get("port"));
            int maxThreads = Integer.parseInt(ConfigReader.Get("maxThreads"));

            Server.InitializeAndStartServer(port, maxThreads);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid port at config.ini -> not a number.");
        }
        catch (Exception e) {
            System.out.println("Could not get parameters from config.ini");
        }
    }
}