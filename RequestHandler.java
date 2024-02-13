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

    public static byte[] HandleClientRequest(String httpRequest) {
        printRequest(httpRequest);

        String defaultPage;
        String defaultRoot;
        try {
            defaultPage = ConfigReader.Get("defaultPage");
            defaultRoot = ConfigReader.Get("root");
        } catch (IOException e) {
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseInternalServerError());
        }

        HttpRequest clientRequest = new HttpRequest(httpRequest, defaultPage, defaultRoot);

        return handleMethod(clientRequest);
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

    private static String getContentType(HttpRequest request)
    {
        if (request.IsImage())
        {
            return HttpResponseHelper.CONTENT_TYPE_IMAGE;
        }
        else if (request.IsIcon())
        {
            return HttpResponseHelper.CONTENT_TYPE_ICON;
        }
        else if (request.IsHtml())
        {
            return HttpResponseHelper.CONTENT_TYPE_HTML;
        }
        else
        {
            return HttpResponseHelper.CONTENT_TYPE_OTHER;
        }
    }

    private static byte[] handleMethod(HttpRequest request)
    {
        if (!request.IsMethodSupported())
        {
            return printHeadersAndReturnResponseByteArray(HttpResponseHelper.ResponseNotImplemented());
        }
        else
        {
            switch (request.Method())
            {
                case GET, POST ->
                {
                    return printHeadersAndReturnResponseByteArray(getResponseFromParsedRequest(request));
                }
                case HEAD ->
                {
                    HttpResponse response = getResponseFromParsedRequest(request);
                    printResponse(new String(response.GetHeaders(), StandardCharsets.UTF_8));
                    return response.GetHeaders();
                }
                case TRACE ->
                {
                    final String STATUS_OK = "200 OK";
                    HttpResponse response = new HttpResponse(STATUS_OK, HttpResponseHelper.CONTENT_TYPE_OTHER, request.OriginalString().getBytes(StandardCharsets.UTF_8));
                    return printHeadersAndReturnResponseByteArray(response);
                }
            }
        }

        // Will never get here.
        return null;
    }

    private static HttpResponse getResponseFromParsedRequest(HttpRequest request)
    {
        byte[] fileData;

        if (!request.IsPathValid())
        {
            return HttpResponseHelper.ResponseNotFound(request.RequestedPage());
        }

        try
        {
            fileData = readFile(new File(request.RequestedPage()));
        }
        catch(IOException e)
        {
            return HttpResponseHelper.ResponseInternalServerError();
        }

        return HttpResponseHelper.ResponseOk(fileData, getContentType(request));
    }
}
