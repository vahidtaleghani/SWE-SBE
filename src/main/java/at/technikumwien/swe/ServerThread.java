package at.technikumwien.swe;

import at.technikumwien.swe.routes.BasicRoute;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread implements Runnable {

    private final Socket connect;

    public ServerThread(Socket c) {
        connect = c;
    }

    public static void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException {

        //Holen eine Eingabeanforderung und analysieren
        Request request = new Request(inputStream);

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

        response.send(outputStream);
    }

    @Override
    public void run() {

        // Tournier check bei jedem Request
        TournamentController.handle();

        try {
            handleRequest(connect.getInputStream(), connect.getOutputStream());
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
