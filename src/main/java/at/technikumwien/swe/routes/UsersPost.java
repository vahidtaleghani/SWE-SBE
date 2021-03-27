package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
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

        TransferUser transferUser;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            transferUser = objectMapper.readValue(request.getPayload(), TransferUser.class);
        } catch (JsonProcessingException e) {
            return Response.Default.invalidJsonProvided();
        }

        UserRepository repository = new UserRepository();

        UserModel user = repository.getUser(transferUser.username);

        if (user != null || !repository.addUser(transferUser.toModel())) {
            return Response.Default.badRequest("User already exists");
        }

        return Response.Default.ok();
    }

    private static class TransferUser {
        @JsonProperty("Username")
        private String username;

        @JsonProperty("Password")
        private String password;

        @Override
        public String toString() {
            return "[username=" + username + ";password=" + password + "]";
        }

        public UserModel toModel() {
            return UserModel.initiateNewUserModel(username, password);
        }
    }
}

