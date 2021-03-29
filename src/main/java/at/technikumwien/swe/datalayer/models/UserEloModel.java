package at.technikumwien.swe.datalayer.models;

import at.technikumwien.swe.datalayer.entities.UserEloEntity;

public class UserEloModel {
    private final UserModel userModel;
    private final int elo;

    public UserEloModel(UserModel userModel, int elo) {
        this.userModel = userModel;
        this.elo = elo;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public int getElo() {
        return elo;
    }

    public UserEloEntity toEntity() {
        return new UserEloEntity(userModel.toEntity(), elo);
    }
}
