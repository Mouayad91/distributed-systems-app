package de.htwsaar.vs.gruppe05.server.DTO;

import lombok.Data;

/**
 * LoginResponseDto capsules the Response after LoginRequest
 * @version 20.02.2023
 */
@Data
public class LoginResponseDTO {
    private String token;

    private String message;

    public LoginResponseDTO(String token, String message){
        this.token = token;
        this.message = message;
    }

}
