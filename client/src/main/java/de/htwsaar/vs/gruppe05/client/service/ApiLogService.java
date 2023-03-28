package de.htwsaar.vs.gruppe05.client.service;

import com.google.gson.Gson;
import de.htwsaar.vs.gruppe05.client.authentication.JwtStorage;
import de.htwsaar.vs.gruppe05.client.model.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiLogService {


    private static final String baseUrl = "http://localhost:8080";
    private static final String path = "/api";
    private static final String url = baseUrl + path;

    private final JwtStorage jwtStorage;

    private final Gson gson;

    public ApiLogService(JwtStorage jwtStorage, Gson gson) {
        this.jwtStorage = jwtStorage;
        this.gson = gson;
    }


    public Log fetchAllLogs(int page, int size) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url + "/logs?page=" + page + "&" + "size=" + size);
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .headers("Authorization", "Bearer " + jwtStorage.getToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Log.class);

    }

}
