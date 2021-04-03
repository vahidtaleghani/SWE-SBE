package at.technikumwien.swe;

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
}
