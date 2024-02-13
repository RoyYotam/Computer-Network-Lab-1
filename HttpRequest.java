import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;

public class HttpRequest {
    /*

        This class represents an HTTP request.

     */

    private static final String SEPERATOR_BETWEEN_REQUEST_PARTS = " ";
    private static final String SEPERATOR_IN_FILE_PATH = "/";
    private static final String SEPERATOR_BETWEEN_FILE_PATH_AND_PARAMS = "\\?";
    private static final String SEPERATOR_BETWEEN_PARAMS = "&";
    private static final String SEPERATOR_BETWEEN_PARAM_NAME_AND_VALUE = "=";


    private final Hashtable<String, String> params = new Hashtable<String, String>();
    // In order to create read-only non-constant fields, I will use
    // private and below an 'get' function.
    private HttpMethod method;
    public HttpMethod Method()
    {
        return method;
    }

    private String requestedPage;
    private final String mainFolderLocation = ".";
    // We will add the '.' to the path start since the folder is
    // referred to the current folder.
    public String RequestedPage()
    {
        return mainFolderLocation + requestedPage;
    }

    //TODO this
    private String referer;
    private String userAgent;

    // Flags
    public boolean IsMethodSupported()
    {
        return method != null;
    }

    public boolean IsPathValid()
    {
        // Does not contain invalid strings
        if (requestedPage.contains("//") || requestedPage.contains("'/../"))
        {
            return false;
        }

        // Starts with /
        if (requestedPage.charAt(0) != '/')
        {
            return false;
        }

        // Checks that path is real path and not directory
        Path filePath = Paths.get(RequestedPage());

        if (!Files.exists(filePath))
        {
            return false;
        }

        if (Files.isDirectory(filePath))
        {
            return false;
        }

        return true;
    }

    private String[] validImageEnds = {".bmp", ".gif", ".png", ".jpg"};
    public boolean IsImage()
    {
        boolean isImage = false;

        for (String imageExtension: validImageEnds)
        {
            if (requestedPage.endsWith(imageExtension))
            {
                isImage = true;
            }
        }
        return isImage;
    }

    // default for file path
    private final String defaultPage;
    private final String defaultRoot;

    // Constructor

    public HttpRequest(String httpRequest, String defaultPage, String defaultRoot)
    {
        this.defaultPage = defaultPage;
        this.defaultRoot = defaultRoot;

        String[] splitRequest = httpRequest.split(SEPERATOR_BETWEEN_REQUEST_PARTS);

        updateMethod(splitRequest[0]);
        updateParams(splitRequest[1]);
        updateFileNameAndPath(splitRequest[1]);
    }

    private void updateMethod(String splitRequestMethod)
    {
        try
        {
            method = HttpMethod.valueOf(splitRequestMethod);
        }
        catch (IllegalArgumentException e)
        {
            method = null;
        }
    }

    private void updateParams(String splitRequestPageUrlAndParams) {
        String[] splitRequest = splitRequestPageUrlAndParams.split(SEPERATOR_BETWEEN_FILE_PATH_AND_PARAMS);

        if (splitRequest.length > 1)
        {
            String[] paramNameAndValuePairsAsStrings = splitRequest[1].split(SEPERATOR_BETWEEN_PARAMS);

            for (String singleParamNameAndValuePair : paramNameAndValuePairsAsStrings)
            {
                String[] paramNameAndValue = singleParamNameAndValuePair.split(SEPERATOR_BETWEEN_PARAM_NAME_AND_VALUE);

                if (paramNameAndValue.length > 1)
                {
                    if (!params.contains(paramNameAndValue[0]))
                    {
                        params.put(paramNameAndValue[0], paramNameAndValue[1]);
                    }
                }
            }
        }
    }

    private void updateFileNameAndPath(String splitRequestPageUrlAndParams) {
        /*
            A function gets request from the client and return the file name in the request.
        */

        String[] splitRequest = splitRequestPageUrlAndParams.split(SEPERATOR_BETWEEN_FILE_PATH_AND_PARAMS);

        if (isDefaultPage(splitRequest[0]))
        {
            requestedPage = defaultRoot + defaultPage;
        } else if (splitRequest[0].startsWith("./")) {
            requestedPage =  splitRequest[0];
        } else
        {
            requestedPage =  splitRequest[0];
        }
    }

    private static boolean isDefaultPage(String httpRequestPageUrl)
    {
        return SEPERATOR_IN_FILE_PATH.equals(httpRequestPageUrl);
    }

    public enum HttpMethod
    {
        GET,
        POST,
        HEAD,
        TRACE
    }
}
