package de.htwsaar.vs.gruppe05.client.authentication;


import de.htwsaar.vs.gruppe05.client.model.enums.RoleEnums;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtStorage {

    private String token;
    private boolean tokenSet;

    public JwtStorage() {
    }

    public void clearToken() {
        this.token = "";
        this.tokenSet = false;
    }

    public void setTokenOnce(String token) {
        if (this.token == null || !token.isEmpty() || !token.isBlank()) {
            this.token = token;
            this.tokenSet = true;
        }
        System.out.println(getRole());
        System.out.println(getUserId() + " = USER ID OF THE LOGGED IN USER");
    }

    public String getToken() {
        return this.token;
    }

    public boolean isTokenSet() {
        return tokenSet;
    }

    public RoleEnums.Role getRole() {
        String bodyEncoded = token.split("\\.")[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String result = new String(decoder.decode(bodyEncoded));
        return (RoleEnums.Role.valueOf(new JSONObject(result).get("role").toString()));
    }

    public Long getUserId() {
        String bodyEncoded = token.split("\\.")[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String result = new String(decoder.decode(bodyEncoded));
        return Long.decode(new JSONObject(result).get("userId").toString());
    }

}
