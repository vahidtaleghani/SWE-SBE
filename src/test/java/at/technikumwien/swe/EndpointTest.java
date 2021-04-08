package at.technikumwien.swe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndpointTest {

    @BeforeEach
    void initialize() throws SQLException {
        TestHelpers.resetDatabase();
        TestHelpers.insertUsers();
        SebServer.initializeRoutes();
    }

    @Test
    void postUsersExists() throws IOException {
        String payload = "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}";
        InputStream inputStream = TestHelpers.generateHttpRequest("POST", "/users", payload, null);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(200, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }

    @Test
    void postUsersInvalidBody1() throws IOException {
        String payload = "";
        InputStream inputStream = TestHelpers.generateHttpRequest("POST", "/users", payload, null);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(400, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }

    @Test
    void postUsersInvalidBody2() throws IOException {
        String payload = "{\"USERNAME\":\"kienboec\", \"Password\":\"daniel\"}";
        InputStream inputStream = TestHelpers.generateHttpRequest("POST", "/users", payload, null);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(400, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }

    @Test
    void authorizedRequestsWorking() throws IOException {
        String token = "user1-sebToken";
        InputStream inputStream = TestHelpers.generateHttpRequest("GET", "/users/user1", null, token);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(200, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }

    @Test
    void unauthorizedRequestsBlocked1() throws IOException {
        String token = "user1-sebTokenWrong";
        InputStream inputStream = TestHelpers.generateHttpRequest("GET", "/users/user1", null, token);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(401, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }

    @Test
    void unauthorizedRequestsBlocked2() throws IOException {
        String token = "user1-sebToken";
        InputStream inputStream = TestHelpers.generateHttpRequest("GET", "/users/user2", null, token);

        OutputStream outputStream = new ByteArrayOutputStream();
        ServerThread.handleRequest(inputStream, outputStream);

        assertEquals(403, TestHelpers.getHttpStatusCode(outputStream.toString()));
    }
}
