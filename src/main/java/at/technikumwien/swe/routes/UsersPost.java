package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;

public class UsersPost implements BasicRoute {

    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.POST && request.getPath().equals("/users");
    }

    @Override
    public Response processRequest(Request request) {
        Response response = new Response();
        response.setStatusCode(Response.StatusCode.OK);
        response.setPayload("Hello World from Users via POST!");
        return response;
    }
}
