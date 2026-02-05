package com.coffee.response;

import org.springframework.http.ResponseCookie;

public record RefreshTokenResponse(String accessToken, ResponseCookie refreshResponseCookie) {
}
