package at.technikumwien.swe;

import at.technikumwien.swe.datalayer.entities.UserEntity;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepository();


    @BeforeEach
    void initDatabase() throws SQLException {
        TestHelpers.resetDatabase();
        TestHelpers.insertUsers();
    }

    @Test
    void getUser() {
        assertNotEquals(null, userRepository.getUser("user1"));
        assertNull(userRepository.getUser("unknownUser"));
    }

    @Test
    void updateUser() {
        assertFalse(userRepository.updateUser(null));

        UserModel user = userRepository.getUser("user1");
        assertNull(user.getImage());

        user.setImage("testImage");
        assertTrue(userRepository.updateUser(user));

        user = userRepository.getUser("user1");
        assertEquals("testImage", user.getImage());
    }

    @Test
    void adduser() {
        UserModel userModel = UserModel.initiateNewUserModel("bahar", "ahdhdfkgjfskfglkdkfg");
        assertTrue(userRepository.addUser(userModel));
        assertNotNull(userRepository.getUser("bahar"));
        assertEquals("bahar", userRepository.getUser("bahar").getUsername());
    }

    @Test
    void userShouldBeUnique() {
        UserModel userModel = UserModel.initiateNewUserModel("bahar", "ahdhdfkgjfskfglkdkfg");
        assertTrue(userRepository.addUser(userModel));
        assertFalse(userRepository.addUser(userModel));
    }

    @Test
    void correctTokenGenerated() {
        UserModel user1 = userRepository.getUser("user1");
        assertEquals("user1-sebToken", user1.getToken());
    }

    @Test
    void ifPasswordIsNotSavedAsPlainText() {
        UserEntity user1 = userRepository.getUser("user1").toEntity();
        assertNotEquals("pw1", user1.getPassword());
    }

    @Test
    void getUserByToken() {
        UserModel user = userRepository.getUserByToken("user1-sebToken");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }
}
