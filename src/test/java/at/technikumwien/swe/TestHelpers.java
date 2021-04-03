package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.Database;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;

import java.sql.SQLException;

public class TestHelpers {

    public static void resetDatabase() throws SQLException {
        Database.getInstance().truncateAllTables();

        UserRepository userRepository = new UserRepository();
        userRepository.addUser(UserModel.initiateNewUserModel("user1", "pw1"));
        userRepository.addUser(UserModel.initiateNewUserModel("user2", "pw2"));
        userRepository.addUser(UserModel.initiateNewUserModel("user3", "pw3"));
    }
}
