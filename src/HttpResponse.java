import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    // Constants
    private static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1 ";
    private static final String CONTENT_TYPE = "content-type: text/html";
    private static final String CONTENT_LENGTH = "content-length: ";

    // Response codes
    // 2xx: Success
    // in case everything is okay.
    private static final String STATUS_OK = "200 OK";
    // 4xx: Client Error
    // if the file was not found.
    private static final String STATUS_NOT_FOUND = "404 Not Found";
    private static final String NOT_FOUND_MESSAGE = "The requested URL %s was not found on this server.";
    // if the request’s format is invalid.
    private static final String STATUS_BAD_REQUEST = "400 Bad Request";
    private static final String BAD_REQUEST_MESSAGE = "The request line contained invalid characters following the protocol string.";
    // 5xx: Server Error
    // if the method used is unknown (a Method is like “GET”).
    private static final String STATUS_NOT_IMPLEMENTED = "501 Not Implemented";
    private static final String NOT_IMPLEMENTED_MESSAGE = "The server did not support the functionality required.";
    // some kind of error.
    private static final String STATUS_INTERNAL_SERVER_ERROR = "500 Internal Server Error";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "The server met an unexpected condition.";

    // Default file
    private static final String DEFAULT_HTML_RESPONSE =
            """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>

                    <h1>%s</h1>
                    <p>%s</p>

                    </body>
                    </html>""";

    public static byte[] ResponseOk(String fileData)
    {
        return createResponse(STATUS_OK, getCustomHtml(STATUS_OK, fileData));
    }

    public static byte[] ResponseNotFound(String url)
    {
        return createResponse(STATUS_NOT_FOUND, getCustomHtml(STATUS_NOT_FOUND, String.format(NOT_FOUND_MESSAGE, url)));
    }

    public static byte[] ResponseBadRequest()
    {
        return createResponse(STATUS_BAD_REQUEST, getCustomHtml(STATUS_BAD_REQUEST, BAD_REQUEST_MESSAGE));
    }

    public static byte[] ResponseNotImplemented()
    {
        return createResponse(STATUS_NOT_IMPLEMENTED, getCustomHtml(STATUS_NOT_IMPLEMENTED, NOT_IMPLEMENTED_MESSAGE));
    }

    public static byte[] ResponseInternalServerError()
    {
        return createResponse(STATUS_INTERNAL_SERVER_ERROR, getCustomHtml(STATUS_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE));
    }

    private static byte[] getCustomHtml(String status, String message)
    {
        return String.format(DEFAULT_HTML_RESPONSE, status, status, message).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] createResponse(String status_code, byte[] fileData)
    {
        byte[] headers =
                (HTTP_VERSION + status_code + CRLF +
                        CONTENT_TYPE + CRLF +
                        CONTENT_LENGTH + (fileData.length) + CRLF +
                        CRLF).getBytes(StandardCharsets.UTF_8);

        byte[] combined = new byte[headers.length + fileData.length];
        System.arraycopy(headers,0,combined,0         ,headers.length);
        System.arraycopy(fileData,0,combined,headers.length,fileData.length);

        return combined;
    }

    private static byte[] readFile(File file)
    {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bFile = new byte[(int)file.length()];

            // read until the end of the stream.
            while(fis.available() != 0)
            {
                fis.read(bFile, 0, bFile.length);
            }

            return bFile;
        }
        catch(FileNotFoundException e) {
            // do something
            int x = 1;
        }
        catch(IOException e)
        {
            // do something
            int x = 3;
        }

        return null;
    }
}
