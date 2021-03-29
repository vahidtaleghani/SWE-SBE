package at.technikumwien.swe.datalayer.entities;

import at.technikumwien.swe.datalayer.models.UserEloModel;

public class UserEloEntity {
    private final UserEntity userEntity;
    private final int elo;

    public UserEloEntity(UserEntity userEntity, int elo) {
        this.userEntity = userEntity;
        this.elo = elo;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public int getElo() {
        return elo;
    }

    public UserEloModel toModel() {
        return new UserEloModel(userEntity.toModel(), elo);
    }
}
