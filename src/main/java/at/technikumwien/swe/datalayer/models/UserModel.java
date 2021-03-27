package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.UserEntity;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class UserModel {
    private static final String SALT = "ASdi2&di.3";
    private final String username, passwordHash, token;
    private String name, biography, image;

    public UserModel(String username, String passwordHash, String token, String name, String biography, String image) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.token = token;
        this.name = name;
        this.biography = biography;
        this.image = image;
    }

    public static UserModel initiateNewUserModel(String username, String password) {
        return new UserModel(
                username,
                hashPassword(password),
                generateToken(username),
                null, null, null
        );
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean validatePassword(String password) {
        if (password == null) return false;
        String hashedPassword = hashPassword(password);
        return (hashedPassword.equals(this.passwordHash));
    }

    public UserEntity toEntity() {
        return new UserEntity(username, passwordHash, token, name, biography, image);
    }

    private static String generateToken(String username) {
        return username + "-sebToken";
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String hashPassword(String password) {
        password = password + SALT;
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
