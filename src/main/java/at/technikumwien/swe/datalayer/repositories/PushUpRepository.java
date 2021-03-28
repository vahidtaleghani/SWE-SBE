package at.technikumwien.swe.datalayer.repositories;

import at.technikumwien.swe.datalayer.daos.PushUpDao;
import at.technikumwien.swe.datalayer.entities.PushUpEntity;
import at.technikumwien.swe.datalayer.models.PushUpModel;

import java.util.List;
import java.util.stream.Collectors;

public class PushUpRepository {

    private final PushUpDao pushUpDao = new PushUpDao();

    public List<PushUpModel> getAll(String username) {
        return pushUpDao.getAll(username)
                .stream().map(PushUpEntity::toModel)
                .collect(Collectors.toList());
    }

    public boolean addPushUp(PushUpModel pushUpModel) {
        return pushUpDao.create(pushUpModel.toEntity());
    }
}
