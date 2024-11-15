package com.mayo.server.auth.app.port.in;

import com.mayo.server.auth.domain.model.RefreshToken;
import com.mayo.server.common.enums.UserType;

public interface RefreshTokenQueryInputPort {

    Long postRefreshToken(Long userId, String token, UserType userType);

    void deleteRefreshToken(Long userId, UserType userType);

    RefreshToken findByUserIdAndUserType(Long userId, UserType userType);
}
