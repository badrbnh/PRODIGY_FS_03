package com.prodigy.server.auth.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author
 **/
@Setter
@Getter
public class TokenResponse {

    private String accessToken;
    private String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
