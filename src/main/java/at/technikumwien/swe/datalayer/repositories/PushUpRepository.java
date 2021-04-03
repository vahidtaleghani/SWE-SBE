package at.technikumwien.swe.datalayer.repositories;

import at.technikumwien.swe.datalayer.daos.PushUpDao;
import at.technikumwien.swe.datalayer.entities.PushUpEntity;
import at.technikumwien.swe.datalayer.models.PushUpModel;
import at.technikumwien.swe.datalayer.models.TournamentState;
import at.technikumwien.swe.datalayer.models.UserModel;

import java.util.List;
import java.util.stream.Collectors;

public class PushUpRepository {

    private final PushUpDao pushUpDao = new PushUpDao();


    public List<PushUpModel> getAll(String username) {
        return pushUpDao.getAll(username)
                .stream().map(PushUpEntity::toModel)
                .collect(Collectors.toList());
    }

    public List<PushUpModel> getAll(TournamentState tournamentState) {
        return pushUpDao.getAll(tournamentState.getValue())
                .stream().map(PushUpEntity::toModel)
                .collect(Collectors.toList());
    }

    public int getSum(UserModel userModel) {
        return pushUpDao.getSum(userModel.toEntity());
    }

    public int getCount(UserModel userModel) {
        return pushUpDao.getCount(userModel.toEntity());
    }

    public boolean addPushUp(PushUpModel pushUpModel) {
        return pushUpDao.create(pushUpModel.toEntity());
    }

    public boolean update(PushUpModel pushUpModel) {
        return pushUpDao.update(pushUpModel.toEntity());
    }
}
