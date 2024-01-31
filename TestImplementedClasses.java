import java.io.DataOutputStream;
import java.net.*;
import java.util.LinkedList;

public class TestImplementedClasses {


    /*

        How to start this test?

        First start the server by clicking the run
        (make sure to run the main of this class below).

        Then, open Chrome app and search (by typing url) the following url
        http://127.0.0.1:9922/
        each time running this (new tab / refresh) will give other response type.


     */
    public static void main(String[]args) throws Exception
    {
        ServerSocket hwSocket = new ServerSocket(9922);
        LinkedList<Thread> threadsList = new LinkedList<>();
        int connectionsSoFar = 0;

        byte[][] responses = {
                HttpResponse.ResponseOk("Some File's Data"),
                HttpResponse.ResponseNotFound("fileThatNotExists.html"),
                HttpResponse.ResponseBadRequest(),
                HttpResponse.ResponseNotImplemented(),
                HttpResponse.ResponseInternalServerError()
        };

        while(true)
        {
            try {
                Socket connectionSocket = hwSocket.accept();

                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                outToClient.write(responses[connectionsSoFar % 5]);
                connectionsSoFar ++;

            } catch (SocketException e) {
                System.err.println(e.getMessage());
                break;
            }
        }
    }
}
