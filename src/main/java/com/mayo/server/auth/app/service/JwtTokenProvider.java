package com.mayo.server.auth.app.service;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.domain.component.RefreshTokenQueryInput;
import com.mayo.server.auth.domain.model.RefreshToken;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String ACCESS_PREFIX_STRING = "Bearer ";
    public static final String REFRESH_PREFIX_STRING = "RefreshToken ";
    private static final String CLAIMS_USER_TYPE = "UserType";
    private static final Long VALIDITY_TIME = 1000L;

    private final SecretKey secretKey;
    private final Long accessTokenValiditySeconds;
    private final Long refreshTokenValiditySeconds;
    private final RefreshTokenQueryInput refreshTokenQueryInputPort;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.access-expire-time}") Long accessTokenValiditySeconds,
                            @Value("${jwt.refresh-expire-time}") Long refreshTokenValiditySeconds,
                            RefreshTokenQueryInput refreshTokenQueryInputPort) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds * VALIDITY_TIME;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds * VALIDITY_TIME;
        this.refreshTokenQueryInputPort = refreshTokenQueryInputPort;
    }

    public JwtToken createJwtToken(Long userId, UserType userType) {
        String accessToken = createAccessToken(String.valueOf(userId), userType);
        String refreshToken = createRefreshToken(userId, userType);
        return new JwtToken(accessToken, refreshToken);
    }

    public JwtToken createChefJwtToken(Long id) {
        String accessToken = ACCESS_PREFIX_STRING + createToken(id, (long) (1000 * 60 * 60 * 24));
        String refreshToken = REFRESH_PREFIX_STRING + createChefRefreshToken(id);
        return new JwtToken(accessToken, refreshToken);
    }

    public Jws<Claims> getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException(ErrorCode.JWT_NOT_FOUND_TOKEN);
        } catch (JwtException ex) {
            throw new TokenExpiredException(ErrorCode.JWT_VALIDATE_ERROR);
        }
    }

    private String createAccessToken(String subject, UserType userType) {
        return ACCESS_PREFIX_STRING + createToken(accessTokenValiditySeconds, subject, userType);
    }

    private String createRefreshToken(Long userId, UserType userType) {
        String token = createToken(refreshTokenValiditySeconds, String.valueOf(userId), userType);
        deleteRefreshToken(userId, userType);
        postRefreshToken(userId, userType, token);

        return token;
    }

    private String createChefRefreshToken(Long id) {
        String token = createToken(id, (long) (1000 * 60 * 60 * 24 * 7));

        RefreshToken originToken = refreshTokenQueryInputPort.findByUserIdAndUserType(id);
        if(Objects.isNull(originToken)) {
            refreshTokenQueryInputPort.postRefreshToken(
                    id,
                    token,
                    UserType.CHEF
            );

            return token;
        }

        refreshTokenQueryInputPort.updateRefreshToken(
                id,
                token,
                UserType.CHEF
        );

        return token;
    }

    private String createToken(Long validityTime, String subject, UserType userType) {
        Date now = new Date();
        Date validTime = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(CLAIMS_USER_TYPE, userType)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createToken(Long id, Long ms) {

        Date now = new Date();
        long expirationTimeMillis = 1000 * 60 * 60 * 24;
        Date validTime = new Date(now.getTime() + expirationTimeMillis);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(CLAIMS_USER_TYPE, UserType.CHEF.name())
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(validTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void deleteRefreshToken(Long userId, UserType userType) {
        refreshTokenQueryInputPort.deleteRefreshToken(userId, userType);
    }

    private void postRefreshToken(Long userId, UserType userType, String token) {
        refreshTokenQueryInputPort.postRefreshToken(userId, token, userType);
    }

    public RefreshToken getRefreshTokenByUserIdAndUserType(Long userId, UserType userType) {
        return refreshTokenQueryInputPort.findByUserIdAndUserType(userId, userType);
    }
}
