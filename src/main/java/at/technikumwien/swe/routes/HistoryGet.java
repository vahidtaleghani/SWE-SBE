package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;


public class HistoryGet extends BasicRoute {
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.GET && request.getPath().equals("/history");
    }

    @Override
    public Response processRequest(Request request) {

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        PushUpRepository repository = new PushUpRepository();

        return Response.Default.json(repository.getAll(loggedUser.getUsername()));
    }

}
