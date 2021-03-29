package at.technikumwien.swe;

import at.technikumwien.swe.routes.BasicRoute;

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
            //Holen eine Eingabeanforderung und analysieren
            Request request = new Request(connect.getInputStream());

            if (request.isValid()) {
                System.out.println("Method: " + request.getMethod());
                System.out.println("path: " + request.getPath());
                System.out.println("httpVersion: " + request.getHttpVersion());
                System.out.println("payload: " + request.getPayload());
                System.out.println("Content-Type: " + request.getContentType());
            }
            Response response = null;
            if (request.isValid()) {

                for (BasicRoute route : SebServer.routeList) {
                    if (route.isResponsibleFor(request)) {
                        response = route.processRequest(request);
                        break;
                    }
                }

                if (response == null) {
                    response = Response.Default.notFound(); // error 404
                }

            } else {
                response = Response.Default.badRequest("Invalid HTTP request");
            }

            response.send(connect.getOutputStream());

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
