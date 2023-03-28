package de.htwsaar.vs.gruppe05.server.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.htwsaar.vs.gruppe05.server.DTO.UserDto;
import de.htwsaar.vs.gruppe05.server.ServerApplication;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.helper.JWTHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Autowired
    private JWTHelper jwtHelper;

    /**
     * Tests user creation, generates token using JWT Helper to get admin authority
     */
    @Test
    public void testCreateUser() {

        String token = jwtHelper.generateToken(1, "admin", StatusEnums.Role.ADMIN);

        HttpHeaders customHeader = new HttpHeaders();

        customHeader.setBearerAuth(token);

        HttpEntity<UserDto> entity = new HttpEntity<>(generateUser(), customHeader);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/users"), HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     *  Tests getting user by ID
     */
    @Test
    public void testGetUserById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/users/1"), HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Tests updating user by creating a user and then updating user
     * @throws JsonProcessingException
     */
    @Test
    public void testUpdateUser() throws JsonProcessingException {

        HttpHeaders customHeader = new HttpHeaders();
        String token = jwtHelper.generateToken(1, "admin", StatusEnums.Role.ADMIN);

        customHeader.setBearerAuth(token);

        UserDto userDto = generateUser();

        HttpEntity<UserDto> entity = new HttpEntity<>(userDto, customHeader);

        ResponseEntity<String> postResponse = restTemplate.exchange(createURLWithPort("/api/users"), HttpMethod.POST, entity, String.class);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        userDto = om.readValue(postResponse.getBody(), UserDto.class);

        userDto.setFirstName("Meister Proper");

        HttpEntity<UserDto> entity2 = new HttpEntity<>(userDto, customHeader);

        ResponseEntity<String> putResponse = restTemplate.exchange(createURLWithPort("/api/users/" + userDto.getId()), HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.ACCEPTED, putResponse.getStatusCode());
    }

    /**
     * Tests deletion by creating a user then deleting the user
     * @throws JsonProcessingException
     */
    @Test
    public void testDeleteUser() throws JsonProcessingException {

        HttpHeaders customHeader = new HttpHeaders();
        String token = jwtHelper.generateToken(1, "admin", StatusEnums.Role.ADMIN);

        customHeader.setBearerAuth(token);

        UserDto userDto = generateUser();

        HttpEntity<UserDto> entity = new HttpEntity<>(userDto, customHeader);

        ResponseEntity<String> postResponse = restTemplate.exchange(createURLWithPort("/api/users"), HttpMethod.POST, entity, String.class);

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        userDto = om.readValue(postResponse.getBody(), UserDto.class);

        HttpEntity<String> entity2 = new HttpEntity<>(null, customHeader);

        ResponseEntity<String> putResponse = restTemplate.exchange(createURLWithPort("/api/users/" + userDto.getId()), HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.NO_CONTENT, putResponse.getStatusCode());
    }

    /**
     * URL builder when making request
     * @param uri
     * @return url path
     */
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    /**
     * Generates random number between 1000 and 10000 for ID
     * @return long
     */
    private long generateNumber() {
        Random r = new Random();
        return r.nextInt(10000 - 1000) + 1000;
    }

    /**
     * Generates userDTO to be used in tests
     * @return UserDto Object
     */
    private UserDto generateUser() {
        String username = UUID.randomUUID().toString();
        return new UserDto(generateNumber(), username, "Test", "Testermann",  username + "@mail.com", "password", LocalDateTime.now(), LocalDateTime.now(), true, StatusEnums.Role.USER);
    }

}
