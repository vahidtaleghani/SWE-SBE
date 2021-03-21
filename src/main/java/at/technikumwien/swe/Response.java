package at.technikumwien.swe;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class Response {

    private StatusCode statusCode;
    private String payload;

    public Response() {
    }

    public Response setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Response setPayload(String payload) {
        this.payload = payload;
        return this;
    }


    public void send(OutputStream outputStream) {

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));

        // Wir senden HTTP-Header mit Daten an den Client
        try {
            out.write("HTTP/1.1 " + statusCode.getLabel() + "\r\n");
            out.write("Server: Java HTTP Server for SWE 1: 1.0\r\n");
            out.write("Date: " + new Date() + "\r\n");
            out.write("Content-Type: text/plain\r\n");

            if (payload != null && !payload.isEmpty()) {
                out.write("Content-Length: " + payload.length() + "\r\n");
                out.write("\r\n"); // blank line between headers and content, very important !
                out.write(payload);
            } else {
                out.write("Content-Length: 0\r\n");
                out.write("\r\n"); // blank line between headers and content, very important !
            }

            out.flush(); // flush für den Ausgabestream des Zeichen leeren
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum StatusCode {

        OK("200 OK"),
        NOT_FOUND("404 Not Found"),
        NOT_IMPLEMENTED("501 Not Implemented"),
        BAD_REQUEST("400 Bad Request"),
        LENGTH_REQUIRED("411 Length Required");

        private final String label;

        StatusCode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }

    public static class Default {
        public static Response notFound() {
            return new Response()
                    .setStatusCode(StatusCode.NOT_FOUND)
                    .setPayload(StatusCode.NOT_FOUND.getLabel());
        }

        public static Response badRequest(String message) {
            return new Response()
                    .setStatusCode(StatusCode.BAD_REQUEST)
                    .setPayload(StatusCode.BAD_REQUEST.getLabel() + "\r\n" + message);
        }

        public static Response badRequest() {
            return badRequest("");
        }
    }

}