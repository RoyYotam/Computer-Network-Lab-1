import java.nio.charset.StandardCharsets;

class HttpResponse {
    private final String status;
    private final String contentType;
    private final byte[] content;

    // Constants
    private static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1 ";
    private static final String CONTENT_TYPE = "content-type: ";
    private static final String CONTENT_LENGTH = "content-length: ";

    public HttpResponse(String status, String contentType, byte[] content) {
        this.status = status;
        this.contentType = contentType;
        this.content = content;
    }

    public byte[] GetHeaders() {
        return
                (HTTP_VERSION + status + CRLF +
                        CONTENT_TYPE + contentType + CRLF +
                        CONTENT_LENGTH + (content.length) + CRLF +
                        CRLF).getBytes(StandardCharsets.UTF_8);
    }

    public byte[] GetResponseByteArray()
    {
        byte[] headers = GetHeaders();

        byte[] combined = new byte[headers.length + content.length];
        System.arraycopy(headers, 0, combined, 0, headers.length);
        System.arraycopy(content, 0, combined, headers.length, content.length);

        return combined;
    }
}