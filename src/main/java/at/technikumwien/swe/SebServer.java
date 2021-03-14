package at.technikumwien.swe;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class SebServer {

    public static final boolean verbose = true;
    // Port zum Abhören der Verbindung
    private static final int PORT = 8000;

    // Clientverbindung über Socket-Klasse

    public static void main(String[] args) {
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
}
