package at.technikumwien.swe.routes;

import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;

public interface BasicRoute {

     boolean isResponsibleFor(Request request);

     Response processRequest(Request request);
}
