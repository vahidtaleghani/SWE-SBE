package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserEloModel;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import at.technikumwien.swe.datalayer.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardGet extends BasicRoute {
    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.GET && request.getPath().equals("/scoreboard");
    }

    @Override
    public Response processRequest(Request request) {

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        UserRepository userRepository = new UserRepository();
        PushUpRepository pushUpRepository = new PushUpRepository();

        List<UserEloModel> userEloModelList = userRepository.getAllUsersWithElo();

        List<UserEloCountTransferObject> userEloCountTransferObjectList = userEloModelList
                .stream().map(userEloModel -> new UserEloCountTransferObject(
                        userEloModel.getUserModel().getUsername(),
                        pushUpRepository.getCount(userEloModel.getUserModel()),
                        userEloModel.getElo()
                ))
                .collect(Collectors.toList());

        return Response.Default.json(userEloCountTransferObjectList);
    }

    private static class UserEloCountTransferObject {
        public final String username;
        public final int totalSubmissions, elo;

        public UserEloCountTransferObject(String username, int totalSubmissions, int elo) {
            this.username = username;
            this.totalSubmissions = totalSubmissions;
            this.elo = elo;
        }
    }
}
