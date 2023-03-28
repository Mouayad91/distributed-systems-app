package de.htwsaar.vs.gruppe05.client.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.ApiError;
import de.htwsaar.vs.gruppe05.client.model.ApiResult;
import de.htwsaar.vs.gruppe05.client.model.LoginResponse;
import de.htwsaar.vs.gruppe05.client.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Component
public class ApiUserService {


    private static final String baseUrl = "http://localhost:8080";
    private static final String path = "/api";
    private static final String url = baseUrl + path;

    private final JwtStorage jwtStorage;

    private final Gson gson;

    public ApiUserService(JwtStorage jwtStorage, Gson gson) {
        this.jwtStorage = jwtStorage;
        this.gson = gson;
    }

    /**
     * Login User
     *
     * @param username the users username
     * @param password the users password
     * @return LoginResponse with contained token and message
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public LoginResponse login(String username, String password) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/login");
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("username=" + username + "&" + "password=" + password))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        /*
        if (response.statusCode() == 401) {
            throw new UnauthorizedException();
        }
    */


        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);

        if (loginResponse.getToken() == null) {

        } else {
            jwtStorage.setTokenOnce(loginResponse.getToken());
        }
        System.out.println(jwtStorage.isTokenSet());
        System.out.println(jwtStorage.getToken());
        return loginResponse;

    }

    /**
     * Register a new user account
     *
     * @param firstName First name of the new user
     * @param lastName  Last name of the new user
     * @param email     email of the new user
     * @param username  username of the new user
     * @param password  passsword of the new user
     * @return Registration response -> Same as login -> Token, Message
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */

    public Boolean register(String firstName, String lastName, String email, String username, String password) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/register");
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("username=" + username + "&password=" + password + "&firstName=" + firstName + "&lastName=" + lastName + "&email=" + email))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);

        if (loginResponse == null || loginResponse.getToken() == null || loginResponse.getToken().equals("null")) {
            return false;
        }

        jwtStorage.setTokenOnce(loginResponse.getToken());
        return true;
    }

    /**
     * Get an user by the userid
     *
     * @param id
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */

    public User getUserById(Long id) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users/" + id);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), User.class);
    }


    /**
     * Get all users
     *
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public List<User> getAllUsers() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users");
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Type listType = new TypeToken<List<User>>() {
        }.getType();
        return gson.fromJson(response.body(), listType);

    }

    /**
     * Admin function
     * Delete an user by its id
     *
     * @param id
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void deleteUserById(Long id) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users/" + id);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .DELETE()
                .build();

        HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Update User function
     * Let users update their own account
     *
     * @param user
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean updateUser(User user) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users");
        String userJson = gson.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtStorage.getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(userJson))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == HttpStatus.ACCEPTED.value();
    }

    /**
     * Admin Endpoint for updating users
     *
     * @param user the user which needs 2 be updated
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */

    public boolean updateUserAdmin(User user) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users/" + user.getId());
        String userJson = gson.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtStorage.getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(userJson))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == HttpStatus.OK.value();
    }

    /**
     * Admin Endpoint 4 creating users
     *
     * @param user the new user
     * @return APIResult information
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public ApiResult<User> createUserAdmin(User user) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/users");
        String userJson = gson.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtStorage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(userJson))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.CREATED.value()) {
            return new ApiResult<User>(Codes.StatusCode.SUCCESS, "", gson.fromJson(response.body(), User.class));
        } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return new ApiResult(Codes.StatusCode.UNAUTHORIZED, "");
        } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            ApiError apiError = gson.fromJson(response.body(), ApiError.class);
            return new ApiResult(Codes.StatusCode.FAILURE, apiError.getMessage());
        } else {
            return new ApiResult(Codes.StatusCode.UNKNOWN, "");
        }
    }
}
