package com.mayo.server.auth.domain.repository;

import com.mayo.server.auth.domain.model.RefreshToken;
import com.mayo.server.common.enums.UserType;

import java.time.LocalDateTime;
import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUserIdAndUserType(Long userId, UserType userType);

    Optional<RefreshToken> findByUserIdAndUserType(Long userId, UserType userType);

    @Modifying
    @Query("UPDATE RefreshToken AS r SET r.refreshToken =:token, r.modifiedAt =:modifiedAt WHERE r.userId = :id AND r.userType = :type")
    void updateRefreshTokenByUserIdAndUserType(
            @Param("token") String token,
            @Param("modifiedAt") LocalDateTime modifiedAt,
            @Param("id") Long id,
            @Param("type") UserType type
    );
}
