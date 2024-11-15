package com.mayo.server.auth.adapter.out.persistence;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}
