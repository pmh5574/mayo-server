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
public class CustomerPhone extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNum;

    private String authCode;

    @Enumerated(EnumType.STRING)
    private CustomerVerificationStatus verificationStatus;

    @Builder
    public CustomerPhone(String phoneNum, String authCode, CustomerVerificationStatus verificationStatus) {
        this.phoneNum = phoneNum;
        this.authCode = authCode;
        this.verificationStatus = verificationStatus;
    }
}
