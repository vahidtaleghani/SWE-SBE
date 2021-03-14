package at.technikumwien.swe;

import java.io.IOException;
import java.net.Socket;


public class ServerThread implements Runnable {

    private final Socket connect;

    public ServerThread(Socket c) {
        connect = c;
    }

    @Override
    public void run() {

        try {
            Request request = new Request(connect.getInputStream());

            if (request.isValid()) {
                System.out.println("Method: " + request.getMethod());
                System.out.println("path: " + request.getPath());
                System.out.println("httpVersion: " + request.getHttpVersion());
                System.out.println("payload: " + request.getPayload());
                System.out.println("Content-Type: " + request.getContentType());
            }


            // ToDo: Handle Request


            connect.close();

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            if (SebServer.verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }
}
