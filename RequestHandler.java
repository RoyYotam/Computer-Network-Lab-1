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
        byte[] fileData = new byte[0];

        print(httpRequest);

        String defaultPage;
        String defaultRoot;
        try
        {
            defaultPage = ConfigReader.Get("defaultPage");
            defaultRoot = ConfigReader.Get("root");
        }
        catch (IOException e)
        {
            response = HttpResponse.ResponseInternalServerError();
            print(Arrays.toString(response));
            return response;
        }

        HttpRequest clientRequest = new HttpRequest(httpRequest, defaultPage, defaultRoot);

        if (!clientRequest.IsMethodSupported())
        {
            response = HttpResponse.ResponseNotImplemented();
            print(Arrays.toString(response));
            return response;
        }

        if (!clientRequest.IsPathValid())
        {
            response = HttpResponse.ResponseNotFound(clientRequest.RequestedPage());
            print(Arrays.toString(response));
            return response;
        }

        try
        {
            fileData = readFile(new File(clientRequest.RequestedPage()));
        }
        catch(IOException e)
        {
            response = HttpResponse.ResponseInternalServerError();
            print(Arrays.toString(response));
            return response;
        }

        response = HttpResponse.ResponseOk(fileData);
        print(Arrays.toString(response));
        return response;
    }

    // This section is handling the printing of the request and the response.
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
