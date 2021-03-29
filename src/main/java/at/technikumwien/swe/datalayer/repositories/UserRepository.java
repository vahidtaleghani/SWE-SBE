package at.technikumwien.swe.datalayer.repositories;

import at.technikumwien.swe.datalayer.daos.UserDao;
import at.technikumwien.swe.datalayer.entities.UserEloEntity;
import at.technikumwien.swe.datalayer.entities.UserEntity;
import at.technikumwien.swe.datalayer.models.UserEloModel;
import at.technikumwien.swe.datalayer.models.UserModel;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {

    private final UserDao userDao = new UserDao();

    public UserModel getUser(String username) {
        UserEntity user = userDao.getOne(username);
        if (user == null) return null;
        return user.toModel();
    }

    public UserModel getUserByToken(String token) {
        UserEntity user = userDao.getOneByToken(token);
        if (user == null) return null;
        return user.toModel();
    }

    public boolean addUser(UserModel userModel) {
        if (userModel == null) return false;
        return userDao.create(userModel.toEntity());
    }

    public boolean updateUser(UserModel userModel) {
        if (userModel == null) return false;
        return userDao.update(userModel.toEntity());
    }

    public UserEloModel getOneWithElo(UserModel userModel) {
        return userDao.getOneWithElo(userModel.toEntity()).toModel();
    }

    public List<UserEloModel> getAllUsersWithElo() {
        return userDao.getAllWithElo()
                .stream().map(UserEloEntity::toModel)
                .collect(Collectors.toList());
    }
}
