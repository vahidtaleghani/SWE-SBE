package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;
import at.technikumwien.swe.datalayer.repositories.UserRepository;

public class StatsGet extends BasicRoute {
    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.GET && request.getPath().equals("/stats");
    }

    @Override
    public Response processRequest(Request request) {

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");


        PushUpRepository pushUpRepository = new PushUpRepository();
        UserRepository userRepository = new UserRepository();

        int totalPushUps = pushUpRepository.getCount(loggedUser);

        int elo = userRepository.getOneWithElo(loggedUser).getElo();


        return Response.Default.json(new UserEloSumTransferObject(
                loggedUser.getUsername(), totalPushUps, elo
        ));
    }

    private static class UserEloSumTransferObject {
        public final String username;
        public final int totalPushUps, elo;

        public UserEloSumTransferObject(String username, int totalPushUps, int elo) {
            this.username = username;
            this.totalPushUps = totalPushUps;
            this.elo = elo;
        }
    }
}
