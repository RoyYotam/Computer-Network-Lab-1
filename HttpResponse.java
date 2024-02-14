import java.nio.charset.StandardCharsets;

class HttpResponse {
    private final String status;
    private final String contentType;
    private final byte[] content;
    private final boolean chunked;

    // Constants
    private static final String CRLF = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1 ";
    private static final String CONTENT_TYPE = "content-type: ";
    private static final String CONTENT_LENGTH = "content-length: ";

    private static final String CHUNKED = "Transfer-Encoding: chunked";

    public boolean IsChunk() {
        return chunked;
    }

    public HttpResponse(String status, String contentType, byte[] content, boolean chunked) {
        this.status = status;
        this.contentType = contentType;
        this.content = content;
        this.chunked = chunked;
    }

    public byte[] GetHeaders() {
        return
                (HTTP_VERSION + status + CRLF +
                        CONTENT_TYPE + contentType + CRLF +
                        ((chunked) ? CHUNKED : CONTENT_LENGTH + (content.length)) + CRLF +
                        CRLF).getBytes(StandardCharsets.UTF_8);
    }

    public byte[][] GetResponseByteArray()
    {
        byte[][] chunkedResponse;
        byte[] headers = GetHeaders();

        if (chunked)
        {
            int chunkLength = 1000;
            int chunks = (int) Math.ceil((double)content.length / chunkLength);
            chunkedResponse = new byte[chunks + 1][chunkLength];

            chunkedResponse[0] = headers;

            for(int i = 0; i < content.length; i += chunkLength)
            {
                int sizeInChunkI = (i + chunkLength > content.length) ? content.length - i : chunkLength;
                System.arraycopy(content, i, chunkedResponse[(i / chunkLength) + 1], 0, sizeInChunkI);
            }
        }
        else
        {
            byte[] combined = new byte[headers.length + content.length];
            System.arraycopy(headers, 0, combined, 0, headers.length);
            System.arraycopy(content, 0, combined, headers.length, content.length);

            chunkedResponse = new byte[][]{combined};
        }

        return chunkedResponse;
    }
}