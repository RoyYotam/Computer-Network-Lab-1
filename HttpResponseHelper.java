import java.nio.charset.StandardCharsets;

public class HttpResponseHelper {

    // Constants
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_IMAGE = "image";
    public static final String CONTENT_TYPE_ICON = "icon";
    public static final String CONTENT_TYPE_OTHER = "application/octet-stream";

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

    public static HttpResponse ResponseOk(byte[] fileData, String contentType)
    {
        return createResponse(STATUS_OK, contentType, fileData);
    }

    public static HttpResponse ResponseNotFound(String url)
    {
        return createResponse(STATUS_NOT_FOUND, CONTENT_TYPE_HTML, getCustomHtml(STATUS_NOT_FOUND, String.format(NOT_FOUND_MESSAGE, url)));
    }

    public static HttpResponse ResponseBadRequest()
    {
        return createResponse(STATUS_BAD_REQUEST, CONTENT_TYPE_HTML, getCustomHtml(STATUS_BAD_REQUEST, BAD_REQUEST_MESSAGE));
    }

    public static HttpResponse ResponseNotImplemented()
    {
        return createResponse(STATUS_NOT_IMPLEMENTED, CONTENT_TYPE_HTML, getCustomHtml(STATUS_NOT_IMPLEMENTED, NOT_IMPLEMENTED_MESSAGE));
    }

    public static HttpResponse ResponseInternalServerError()
    {
        return createResponse(STATUS_INTERNAL_SERVER_ERROR, CONTENT_TYPE_HTML, getCustomHtml(STATUS_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE));
    }

    private static byte[] getCustomHtml(String status, String message)
    {
        return String.format(DEFAULT_HTML_RESPONSE, status, status, message).getBytes(StandardCharsets.UTF_8);
    }

    private static HttpResponse createResponse(String status_code, String contentType, byte[] fileData)
    {
        return new HttpResponse(status_code, contentType, fileData);
    }
}
