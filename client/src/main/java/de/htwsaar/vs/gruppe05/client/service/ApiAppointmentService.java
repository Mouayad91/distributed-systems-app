package de.htwsaar.vs.gruppe05.client.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiAppointmentService {


    private static final String baseUrl = "http://localhost:8080/api";
    private static final String path = "/appointments";
    private static final String url = baseUrl + path;


    private final JwtStorage jwtStorage;

    private final Gson gson;

    public ApiAppointmentService(JwtStorage jwtStorage, Gson gson) {
        this.jwtStorage = jwtStorage;
        this.gson = gson;
    }


    /**
     * Get all appointments [ADMIN]
     *
     * @return
     * @throws IOException
     */
    public List<Appointment> getAllAppointmentsAdmin() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(url + "");
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<Appointment>>() {
            }.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * Get all appointments of user [ADMIN]
     *
     * @param user
     * @return
     */
    public List<Appointment> getAllAppointmentsOfUserAdmin(User user) throws URISyntaxException, IOException, InterruptedException {
        List<Appointment> appointments = new ArrayList<>();

        Gson gson = new Gson();
        URI uri = new URI(url + "/all/" + user.getId());
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Appointment[] loginResponse = gson.fromJson(response.body(), Appointment[].class);


        return appointments;
    }

    /**
     * Get all appointments of userId
     *
     * @param userId
     * @return
     */
    public List<Appointment> getAllAppointmentsOfUserId(Long userId) {
        List<Appointment> appointments = new ArrayList<>();
        return appointments;
    }


    /**
     * Get all appointments from
     *
     * @return a list of appointments
     */
    public List<Appointment> getAllAppointments() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(url + "/all");
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<Appointment>>() {
            }.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            return new ArrayList<>();
        }
    }


    public ApiResult<Appointment> createAppointment(Appointment appointment) throws URISyntaxException, IOException, InterruptedException {
        //.setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        URI uri = new URI(url + "");
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(appointment)))
                .build();

        System.out.println(HttpRequest.BodyPublishers.ofString(gson.toJson(appointment)));
        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == HttpStatus.CREATED.value()) {
            return new ApiResult<Appointment>(Codes.StatusCode.SUCCESS, "Created", gson.fromJson(response.body(), Appointment.class));
        } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return new ApiResult<>(Codes.StatusCode.UNAUTHORIZED, "");
        } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            ApiError apiError = gson.fromJson(response.body(), ApiError.class);
            return new ApiResult<>(Codes.StatusCode.FAILURE, apiError.getMessage());
        } else {
            return new ApiResult<>(Codes.StatusCode.UNKNOWN, "");
        }
    }

    public Codes.StatusCode deleteAppointment(Long id) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(baseUrl + "/appointments/" + id);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return Codes.StatusCode.SUCCESS;
    }

    public ApiResult<Appointment> updateAppointment(Appointment appointment) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/" + appointment.getId());
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(appointment)))
                .build();

        System.out.println(HttpRequest.BodyPublishers.ofString(gson.toJson(appointment)));
        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == HttpStatus.CREATED.value()) {
            return new ApiResult<Appointment>(Codes.StatusCode.SUCCESS, "Created", gson.fromJson(response.body(), Appointment.class));
        } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.value()) {
            return new ApiResult<>(Codes.StatusCode.UNAUTHORIZED, "");
        } else if (response.statusCode() == HttpStatus.CONFLICT.value()) {
            ApiError apiError = gson.fromJson(response.body(), ApiError.class);
            return new ApiResult<>(Codes.StatusCode.FAILURE, apiError.getMessage());
        } else {
            return new ApiResult<>(Codes.StatusCode.UNKNOWN, "");
        }
    }

    public List<User> getAllInvitedUserToAppointment(Long appointmentId) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/" + appointmentId + "/users");
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(gson.fromJson(response.body(), User[].class));
    }

    public List<Invitation> getAllInvitationsToAppointment(Long appointmentId) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/" + appointmentId + "/invitations");
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return Arrays.asList(gson.fromJson(response.body(), Invitation[].class));
    }


}
