package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import at.technikumwien.swe.datalayer.repositories.UserRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class TestHelpers {

    public static void resetDatabase() throws SQLException {
        Database.getInstance().truncateAllTables();
    }

    public static void insertUsers() {
        UserRepository userRepository = new UserRepository();
        userRepository.addUser(UserModel.initiateNewUserModel("user1", "pw1"));
        userRepository.addUser(UserModel.initiateNewUserModel("user2", "pw2"));
        userRepository.addUser(UserModel.initiateNewUserModel("user3", "pw3"));
    }

    public static void insertPushUps() {
        PushUpRepository pushUpRepository = new PushUpRepository();
        pushUpRepository.addPushUp(new PushUpModel("user1", "Training", 10, 20));
        pushUpRepository.addPushUp(new PushUpModel("user1", "Training", 20, 30));
        pushUpRepository.addPushUp(new PushUpModel("user2", "Workout", 15, 40));
        pushUpRepository.addPushUp(new PushUpModel("user2", "Workout", 10, 60));
    }

    public static void updatePushUpState() {
        PushUpRepository pushUpRepository = new PushUpRepository();
        List<PushUpModel> pushUpList = pushUpRepository.getAll("user1");
        for (PushUpModel pushUp : pushUpList) {
            pushUp.setTournamentState(TournamentState.WIN);
            pushUpRepository.update(pushUp);
        }
        pushUpList = pushUpRepository.getAll("user2");
        for (PushUpModel pushUp : pushUpList) {
            pushUp.setTournamentState(TournamentState.LOSE);
            pushUpRepository.update(pushUp);
        }
    }


    public static InputStream generateHttpRequest(String method, String path, String payload, String authToken) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.US_ASCII));

        writer.print(method + " " + path + " HTTP/1.1\r\n");
        writer.print("Host: localhost\r\n");
        if (payload != null) {
            writer.print("Content-Type: application/json\r\n");
            writer.print("Content-Length: " + payload.length() + "\r\n");
        }
        if (authToken != null) {
            writer.print("Authorization: Basic " + authToken + "\r\n");
        }
        writer.print("\r\n");
        if (payload != null) writer.print(payload);

        writer.flush();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static int getHttpStatusCode(String httpResponse) {
        String firstLine = httpResponse.lines().findFirst().get();

        String httpCode = firstLine.split(" ")[1];

        return Integer.parseInt(httpCode);
    }
}
