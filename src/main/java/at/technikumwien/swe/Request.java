package at.technikumwien.swe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Request {

    private final Map<String, String> headers = new HashMap<>();
    private HttpMethod method = null;
    private ContentType contentType = null;
    private String path = null;
    private String httpVersion = null;
    private String payload = null;

    public Request(InputStream inputStream) {

        // Wir lesen Zeichen vom Client über den Eingabestream am Socket
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        // Holen Sie sich die erste Zeile der Anfrage vom Client
        String line;
        try {
            line = in.readLine();

            if (line == null) return;

            // Wir analysieren die Anfrage mit einem String-Tokenizer
            try {
                StringTokenizer parse = new StringTokenizer(line);
                method = HttpMethod.fromValue(parse.nextToken()); // Wir erhalten die HTTP-Methode des Clients
                path = parse.nextToken(); // Wir bekommen eine angeforderte Datei
                httpVersion = parse.nextToken();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }

            line = in.readLine();
            while (!line.isEmpty()) {

                String[] split = line.split(": ", 2);
                if (split.length == 2) {
                    headers.put(split[0].toLowerCase(), split[1]);
                }

                line = in.readLine();
            }

            if (headers.containsKey("content-length")
                    && headers.containsKey("content-type")
            ) {
                contentType = ContentType.fromValue(headers.get("content-type"));
                int length = Integer.parseInt(headers.get("content-length"));
                char[] charBuff = new char[length];
                int hasRead = in.read(charBuff, 0, length);
                if (hasRead != length) {
                    System.out.println("Payload was not the expected length: " + hasRead);
                }
                payload = new String(charBuff);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return method != null && httpVersion != null && path != null;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getPayload() {
        return payload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
