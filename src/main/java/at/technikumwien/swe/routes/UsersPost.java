package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsersPost implements BasicRoute {

    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.POST && request.getPath().equals("/users");
    }

    @Override
    public Response processRequest(Request request) {

        // Json to Java Object POJO

        ObjectMapper objectMapper = new ObjectMapper();

        TransferUser tUser = null;
        try {
            tUser = objectMapper.readValue(request.getPayload(), TransferUser.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(tUser);


        Response response = new Response();
        response.setStatusCode(Response.StatusCode.OK);
        response.setPayload("Hello World from Users via POST!");


        return response;
    }
}


class TransferUser {
    @JsonProperty("Username")
    public String username;

    @JsonProperty("Password")
    public String password;

    @Override
    public String toString() {
        return "[username=" + username + ";password=" + password + "]";
    }
}