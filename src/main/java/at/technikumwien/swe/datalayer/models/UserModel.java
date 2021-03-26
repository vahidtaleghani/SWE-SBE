package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.UserEntity;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class UserModel {
    private static final String SALT = "ASdi2&di.3";
    private final String username, passwordHash, token;

    public String getUsername() {
        return username;
    }

    public UserModel(String username, String passwordHash, String token) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.token = token;
    }

    public static UserModel initiateNewUserModel(String username, String password) {
        return new UserModel(
                username,
                hashPassword(password),
                generateToken(username)
        );
    }

    private static String generateToken(String username) {
        return username + "-sebToken";
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String hashPassword(String password) {
        password = password + SALT;
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserEntity toEntity() {
        return new UserEntity(username, passwordHash, token);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
