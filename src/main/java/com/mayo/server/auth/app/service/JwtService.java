package com.mayo.server.auth.app.service;


import static com.mayo.server.auth.app.service.JwtTokenProvider.ACCESS_PREFIX_STRING;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.domain.model.RefreshToken;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken createChefJwtToken(Long id) {

        return jwtTokenProvider.createChefJwtToken(id);
    }

    public JwtToken createJwtToken(Long id, UserType userType) {
        return jwtTokenProvider.createJwtToken(id, userType);
    }

    public void deleteRefreshToken(Long userId, UserType userType) {
        jwtTokenProvider.deleteRefreshToken(userId, userType);
    }

    public JwtToken reissueAccessToken(String token) {
        Jws<Claims> tokenClaims = getJwt(token);

        Long userId = Long.valueOf(tokenClaims.getBody().getSubject());
        UserType userType = UserType.valueOf(tokenClaims.getBody().get("UserType", String.class));

        RefreshToken refreshToken = jwtTokenProvider.getRefreshTokenByUserIdAndUserType(userId, userType);

        if (!refreshToken.getUserId().equals(userId) || !refreshToken.getUserType().equals(userType)) {
            throw new TokenExpiredException(ErrorCode.JWT_VALIDATE_ERROR);
        }

        return jwtTokenProvider.createJwtToken(refreshToken.getUserId(), refreshToken.getUserType());
    }

    private Jws<Claims> getJwt(final String token) {
        return jwtTokenProvider.getTokenClaims(token);
    }

    private String getTokenWithoutBearer(final String token) {
        return Arrays.stream(token.split(ACCESS_PREFIX_STRING))
                .filter(s -> !s.trim().isEmpty())
                .findFirst()
                .orElse("");
    }

    public Long getJwtUserId(final String token) {
        String tokenWithoutBearer = getTokenWithoutBearer(token);
        Jws<Claims> jwt = getJwt(tokenWithoutBearer);
        return Long.valueOf(jwt.getBody().getSubject());
    }
}
