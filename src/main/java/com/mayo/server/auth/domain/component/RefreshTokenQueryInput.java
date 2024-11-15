package com.mayo.server.auth.domain.component;

import com.mayo.server.auth.app.port.in.RefreshTokenQueryInputPort;
import com.mayo.server.auth.domain.model.RefreshToken;
import com.mayo.server.auth.domain.repository.RefreshTokenRepository;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.DateUtility;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class RefreshTokenQueryInput implements RefreshTokenQueryInputPort {

    private final RefreshTokenRepository refreshTokenRepository;

    public Long postRefreshToken(Long userId, String token, UserType userType) {
        return refreshTokenRepository.save(RefreshToken.builder()
                        .refreshToken(token)
                        .userId(userId)
                        .userType(userType)
                        .build())
                .getId();
    }

    public void updateRefreshToken(Long id, String token, UserType userType) {

        LocalDateTime modifiedAt = DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss);
        refreshTokenRepository.updateRefreshTokenByUserIdAndUserType(
                token,
                modifiedAt,
                id,
                userType
        );
    }

    public void deleteRefreshToken(Long userId, UserType userType) {
        refreshTokenRepository.findByUserIdAndUserType(userId, userType)
                .ifPresent(refreshTokenRepository::delete);
    }

    public RefreshToken findByUserIdAndUserType(Long userId, UserType userType) {
        return refreshTokenRepository.findByUserIdAndUserType(userId, userType)
                .orElseThrow(() -> new NotFoundException(ErrorCode.JWT_VALIDATE_ERROR));
    }

    public RefreshToken findByUserIdAndUserType(Long id) {
        return refreshTokenRepository.findByUserIdAndUserType(id, UserType.CHEF)
                .orElse(null);
    }
}
