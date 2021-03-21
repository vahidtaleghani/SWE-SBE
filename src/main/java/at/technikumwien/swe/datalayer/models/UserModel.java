package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.UserEntity;

public class UserModel {
    private final String username, password;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity toEntity() {
        return new UserEntity(username, password);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
