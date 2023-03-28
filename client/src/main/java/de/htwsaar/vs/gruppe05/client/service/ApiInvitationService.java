package de.htwsaar.vs.gruppe05.client.service;

import com.google.gson.Gson;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.ApiError;
import de.htwsaar.vs.gruppe05.client.model.ApiResult;
import de.htwsaar.vs.gruppe05.client.model.Invitation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiInvitationService {

    private static final String baseUrl = "http://localhost:8080";
    private static final String path = "/api/invitations";
    private static final String url = baseUrl + path;

    private final JwtStorage jwtStorage;

    private final Gson gson;

    public ApiInvitationService(JwtStorage jwtStorage, Gson gson) {
        this.jwtStorage = jwtStorage;
        this.gson = gson;
    }


    /**
     * Get all invitations from authenticated users
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public List<Invitation> getInvitations() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(gson.fromJson(response.body(), Invitation[].class));
    }


    public ApiResult createInvitation(Invitation invitation) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(invitation)))
                .build();


        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            return new ApiResult<Invitation>(Codes.StatusCode.SUCCESS, "Created", gson.fromJson(response.body(), Invitation.class));
        } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return new ApiResult<>(Codes.StatusCode.UNAUTHORIZED, "");
        } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            ApiError apiError = gson.fromJson(response.body(), ApiError.class);
            return new ApiResult<ApiError>(Codes.StatusCode.FAILURE, apiError.getMessage());
        } else {
            return new ApiResult<>(Codes.StatusCode.UNKNOWN, "");
        }
    }


    public void deleteInvitation(Long id) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/" + id);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .DELETE()
                .build();

        HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public ApiResult updateInvitation(Invitation invitation) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/" + invitation.getId());
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(invitation)))
                .build();


        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            return new ApiResult<Invitation>(Codes.StatusCode.SUCCESS, "Created", gson.fromJson(response.body(), Invitation.class));
        } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return new ApiResult<>(Codes.StatusCode.UNAUTHORIZED, "");
        } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            ApiError apiError = gson.fromJson(response.body(), ApiError.class);
            return new ApiResult<ApiError>(Codes.StatusCode.FAILURE, apiError.getMessage());
        } else {
            return new ApiResult<>(Codes.StatusCode.UNKNOWN, "");
        }
    }


}
