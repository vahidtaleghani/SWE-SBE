package at.technikumwien.swe.routes;

import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;

public abstract class BasicRoute {

    abstract public boolean isResponsibleFor(Request request);

    abstract public Response processRequest(Request request);

    public UserModel validateToken(String token) {
        if (token == null) return null;
        String authTokenPrefix = "Basic ";
        if (!token.startsWith(authTokenPrefix)) return null;
        if (token.length() <= authTokenPrefix.length()) return null;

        token = token.substring(authTokenPrefix.length()); // remove the "Basic " in the beginning

        UserRepository userRepository = new UserRepository();

        return userRepository.getUserByToken(token);
    }
}
