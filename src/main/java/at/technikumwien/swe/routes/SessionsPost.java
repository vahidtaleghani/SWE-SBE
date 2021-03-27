package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionsPost implements BasicRoute {

    @Override
    public boolean isResponsibleFor(Request request) {
        return request.getMethod() == HttpMethod.POST && request.getPath().equals("/sessions");
    }

    @Override
    public Response processRequest(Request request) {

        TransferUser transferUser;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            transferUser = objectMapper.readValue(request.getPayload(), TransferUser.class);
        } catch (JsonProcessingException e) {
            return Response.Default.invalidJsonProvided();
        }

        UserRepository repository = new UserRepository();

        UserModel user = repository.getUser(transferUser.username);

        if (user == null || !user.validatePassword(transferUser.password)) {
            return Response.Default.unauthorized("Wrong credentials");
        }

        return Response.Default.json(user.getToken());
    }

    private static class TransferUser {
        @JsonProperty("Username")
        private String username;

        @JsonProperty("Password")
        private String password;
    }
}
