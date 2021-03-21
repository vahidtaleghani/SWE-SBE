package at.technikumwien.swe;

import at.technikumwien.swe.routes.BasicRoute;
import at.technikumwien.swe.routes.SessionsPost;
import at.technikumwien.swe.routes.UsersPost;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SebServer {

    public static final boolean verbose = true;
    // Port zum Abhören der Verbindung
    private static final int PORT = 8000;

    public static final List<BasicRoute> routeList = new LinkedList<>();

    // Clientverbindung über Socket-Klasse

    public static void main(String[] args) {

        SebServer.initializeRoutes();

        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

            // Wir hören zu, bis der Benutzer die Serverausführung anhält
            while (!serverConnect.isClosed()) {
                ServerThread myServer = new ServerThread(serverConnect.accept());

                if (verbose) {
                    System.out.println("Connection opened. (" + new Date() + ")");
                }

                //Erstellen Sie einen dedizierten Thread, um die Clientverbindung zu verwalten
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

    public static void initializeRoutes() {
        routeList.add(new UsersPost());
        routeList.add(new SessionsPost());
    }
}