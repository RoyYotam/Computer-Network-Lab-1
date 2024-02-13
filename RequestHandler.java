import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        HttpResponse response;
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
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseInternalServerError());
        }

        HttpRequest clientRequest = new HttpRequest(httpRequest, defaultPage, defaultRoot);

        if (!clientRequest.IsMethodSupported())
        {
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseNotImplemented());
        }

        if (!clientRequest.IsPathValid())
        {
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseNotFound(clientRequest.RequestedPage()));
        }

        try
        {
            fileData = readFile(new File(clientRequest.RequestedPage()));
        }
        catch(IOException e)
        {
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseInternalServerError());
        }

        return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseOk(fileData));
    }

    // This section is handling the printing of the request and the response.
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

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

    private static byte[] printHeadersAndReturnResponseByteArray(HttpResponse response)
    {
        printResponse(new String(response.GetHeaders(), StandardCharsets.UTF_8));
        return response.GetResponseByteArray();
    }
}
