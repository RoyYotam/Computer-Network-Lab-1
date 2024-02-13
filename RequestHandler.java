import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RequestHandler {

    /*
        This class handle the request from the user,
        examples of a request:

        1.
        GET /index.html HTTP/1.0[CRLF]
        [CRLF]

        2.
        GET /index.html?x=1&y=2 HTTP/1.0[CRLF] [CRLF]

        3.
        POST /index.html?x=1&y=2 HTTP/1.0[CRLF] Content-length: 3
        [CRLF]
        z=8

     */

    public static byte[] HandleClientRequest(String httpRequest)
    {
        byte[] response;
        byte[] fileData;

        printRequest(httpRequest);

        String defaultPage;
        String defaultRoot;
        try
        {
            defaultPage = ConfigReader.Get("defaultPage");
            defaultRoot = ConfigReader.Get("root");
        }
        catch (IOException e)
        {
            System.out.println("case 1");
            response = HttpResponse.ResponseInternalServerError();
            printResponse(new String(response, StandardCharsets.UTF_8));
            return response;
        }

        HttpRequest clientRequest = new HttpRequest(httpRequest, defaultPage, defaultRoot);

        if (!clientRequest.IsMethodSupported())
        {
            System.out.println("case 2");
            response = HttpResponse.ResponseNotImplemented();
            printResponse(new String(response, StandardCharsets.UTF_8));
            return response;
        }

        if (!clientRequest.IsPathValid())
        {
            System.out.println("case 3");
            response = HttpResponse.ResponseNotFound(clientRequest.RequestedPage());
            printResponse(new String(response, StandardCharsets.UTF_8));
            return response;
        }

        try
        {
            fileData = readFile(new File(clientRequest.RequestedPage()));
        }
        catch(IOException e)
        {
            System.out.println("case 4");
            response = HttpResponse.ResponseInternalServerError();
            printResponse(new String(response, StandardCharsets.UTF_8));
            return response;
        }

        System.out.println("case 5");
        response = HttpResponse.ResponseOk(fileData);
        printResponse(new String(response, StandardCharsets.UTF_8));
        return response;
    }

    // This section is handling the printing of the request and the response.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static void printRequest(String httpRequest)
    {
        print(ANSI_CYAN + httpRequest + ANSI_RESET);
    }

    private static void printResponse(String httpResponse)
    {
        print(ANSI_GREEN + httpResponse + ANSI_RESET);
    }

    private static void print(String httpRequestOrResponse)
    {
        System.out.println(httpRequestOrResponse);
    }

    // ReadFile as got from the pdf.
    private static byte[] readFile(File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        byte[] bFile = new byte[(int)file.length()];

        // read until the end of the stream.
        while(fis.available() != 0)
        {
            fis.read(bFile, 0, bFile.length);
        }

        return bFile;
    }
}
