package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.PushUpRepository;


public class HistoryDelete extends BasicRoute {
    private static final String routePrefix = "/users/";

    @Override
    public boolean isResponsibleFor(Request request) {
        return
                request.getMethod() == HttpMethod.DELETE &&
                        request.getPath().startsWith(routePrefix) &&
                        request.getPath().length() > routePrefix.length();
    }

    @Override
    public Response processRequest(Request request) {

        String requestedUserName = request.getPath().substring(routePrefix.length());

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        if (!loggedUser.getUsername().equals(requestedUserName)) {
            return Response.Default.unauthorized("You are not authorized to delete push_up");
        }

        PushUpRepository repository = new PushUpRepository();

        if (!repository.delete(loggedUser)) {
            return Response.Default.internalServerError();
        }

        return Response.Default.ok();
    }
}
