import java.io.*;
import java.net.Socket;

public class FaviconHandler {
    private static final String FAVICON_PATH = "../../www/lab/html/favicon.ico";

    public static void handleFavicon(Socket clientSocket) {
        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            File faviconFile = new File(FAVICON_PATH);

            if (faviconFile.exists()) {
                byte[] faviconContent = readFile(faviconFile);
                sendFaviconResponse(outputStream, faviconContent);
            } else {
                sendErrorResponse(outputStream, 404); // Not Found
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendFaviconResponse(OutputStream outputStream, byte[] content) throws IOException {
        String responseHeader = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: image/x-icon\r\n" +
                                "Content-Length: " + content.length + "\r\n\r\n";

        outputStream.write(responseHeader.getBytes());
        outputStream.write(content);
        outputStream.flush();
    }

    private static void sendErrorResponse(OutputStream outputStream, int statusCode) throws IOException {
        String responseHeader = "HTTP/1.1 " + statusCode + " Not Found\r\n" +
                                "Content-Type: text/plain\r\n\r\n" +
                                "Error " + statusCode;

        outputStream.write(responseHeader.getBytes());
        outputStream.flush();
    }

    private static byte[] readFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bFile = new byte[(int) file.length()];
            fis.read(bFile);
            return bFile;
        }
    }
}