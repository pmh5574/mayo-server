package com.mayo.server.auth.domain.model;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.common.enums.UserType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Builder
@Table(name = "refresh_token")
@AllArgsConstructor
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @Tsid
    private Long id;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Long userId;

    private LocalDateTime deletedAt;

    @Builder
    public RefreshToken(String refreshToken, UserType userType, Long userId) {
        this.refreshToken = refreshToken;
        this.userType = userType;
        this.userId = userId;
    }
}
