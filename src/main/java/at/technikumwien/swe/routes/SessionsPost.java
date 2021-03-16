package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;

public class SessionsPost implements BasicRoute {

    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.POST && request.getPath().equals("/sessions");
    }

    @Override
    public Response processRequest(Request request) {
        return null;
    }
}
