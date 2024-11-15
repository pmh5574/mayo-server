package com.mayo.server.customer.domain.model;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CustomerEmail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authCode;

    @Enumerated(EnumType.STRING)
    private CustomerVerificationStatus verificationStatus;

    @Builder
    public CustomerEmail(String email, String authCode, CustomerVerificationStatus verificationStatus) {
        this.email = email;
        this.authCode = authCode;
        this.verificationStatus = verificationStatus;
    }
}
