package at.technikumwien.swe.routes;

import at.technikumwien.swe.HttpMethod;
import at.technikumwien.swe.Request;
import at.technikumwien.swe.Response;
import at.technikumwien.swe.datalayer.models.UserModel;
import at.technikumwien.swe.datalayer.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsersPut extends BasicRoute {
    private static final String routePrefix = "/users/";

    @Override
    public boolean isResponsibleFor(Request request) {
        return
                request.getMethod() == HttpMethod.PUT &&
                        request.getPath().startsWith(routePrefix) &&
                        request.getPath().length() > routePrefix.length();
    }

    @Override
    public Response processRequest(Request request) {

        String requestedUserName = request.getPath().substring(routePrefix.length());

        UserModel loggedUser = validateToken(request.getHeaders().get("authorization"));
        if (loggedUser == null) return Response.Default.unauthorized("No valid authorization token provided.");

        if (!loggedUser.getUsername().equals(requestedUserName)) {
            return Response.Default.unauthorized("You are not authorized to view this profile");
        }

        TransferUser newUserDetails;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            newUserDetails = objectMapper.readValue(request.getPayload(), TransferUser.class);
        } catch (JsonProcessingException e) {
            return Response.Default.invalidJsonProvided();
        }

        loggedUser.setName(newUserDetails.name);
        loggedUser.setBiography(newUserDetails.biography);
        loggedUser.setImage(newUserDetails.image);

        System.out.println(loggedUser);

        UserRepository repository = new UserRepository();

        if (!repository.updateUser(loggedUser)) {
            return Response.Default.internalServerError();
        }

        return Response.Default.ok();
    }

    @SuppressWarnings("FieldMayBeFinal")
    private static class TransferUser {

        @JsonProperty("Name")
        private String name = null;

        @JsonProperty("Bio")
        private String biography = null;

        @JsonProperty("Image")
        private String image = null;
    }
}
