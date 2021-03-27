package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.UserModel;

public class UserEntity {
    private final String username, password, token, name, biography, image;

    public UserEntity(String username, String password, String token, String name, String biography, String image) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.name = name;
        this.biography = biography;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public String getImage() {
        return image;
    }

    public UserModel toModel() {
        return new UserModel(username, password, token, name, biography, image);
    }
}
