package de.htwsaar.vs.gruppe05.server.rest;

import de.htwsaar.vs.gruppe05.server.DTO.LoginResponseDTO;
import de.htwsaar.vs.gruppe05.server.enums.StatusEnums;
import de.htwsaar.vs.gruppe05.server.helper.JWTHelper;
import de.htwsaar.vs.gruppe05.server.model.User;
import de.htwsaar.vs.gruppe05.server.repository.UserRepository;
import de.htwsaar.vs.gruppe05.server.service.LogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * AuthController
 * Provides /login and /register endpoints
 *
 * @version 20.02.2023
 */
@RestController
public class AuthController {
    private final UserRepository userRepository;

    private final LogService logService;

    private final JWTHelper jwtHelper;
    
    

    public AuthController(UserRepository userRepository, JWTHelper jwtHelper, LogService logService) {
        this.userRepository = userRepository;
        this.jwtHelper = jwtHelper;
        this.logService = logService;

    }

    /**
     * User Login Endpoint
     * @param username the username of the user
     * @param password the plain text password of the user
     * @return the Object
     */
    @PostMapping("/api/login")
    public ResponseEntity<Object> loginUser(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {

        if (username.length() < 3 || password.length() < 5) {
            return new ResponseEntity<Object>("Bad Request, invalid username / password length", HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 14);
        String encryptedPassword = encoder.encode(password);

        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {

            if (encoder.matches(password, user.get().getPassword())) {

                // if -> Create JWT, load claims, save to db, ... ? LAST LOGIN TIME, LOGS ?
                user.get().setLastLoginDate(LocalDateTime.now());
                userRepository.save(user.get());
                return new ResponseEntity<>(new LoginResponseDTO(jwtHelper.generateToken(user.get().getId(), user.get().getUserName(),user.get().getRole()),"Success"), HttpStatus.OK);


            } else {
                return new ResponseEntity<>(new LoginResponseDTO(null,"Invalid username or Password"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(new LoginResponseDTO(null,"Username could not be found"), HttpStatus.NO_CONTENT);
        }
        // LOGIC
        // Check if username exists with password in combination
        // if not -> return FORBIDDEN/UNAUTHORIZED;
    }


    /**
     * User Register Endpoint
     * @param username the username of the user (must be unique)
     * @param email the email of the user ( must be unique)
     * @param password the password of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @return the status string
     */
    @PostMapping("/api/register")
    public ResponseEntity<Object> registeruser(@RequestParam(name = "username") String username, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName) {
        if (username.length() < 3 || password.length() < 5 || firstName.isEmpty() || lastName.isEmpty()) {
            return new ResponseEntity<>(new LoginResponseDTO(null,"Invalid Parameters"),HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByUserName(username).isPresent()) {
            return new ResponseEntity<>(new LoginResponseDTO(null,"Username already taken"),HttpStatus.BAD_REQUEST);
        }
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 14);

        User user = new User(username, email, firstName, lastName);
        user.setRole(StatusEnums.Role.USER);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);

        Optional<User> us = userRepository.findByUserName(username);

        return new ResponseEntity<>(new LoginResponseDTO(jwtHelper.generateToken(us.get().getId(),us.get().getUserName(),us.get().getRole()), "Success"),HttpStatus.CREATED);
    }
}
