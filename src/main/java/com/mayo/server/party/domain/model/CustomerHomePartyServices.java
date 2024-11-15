package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.party.domain.enums.CustomerPartyServices;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CustomerHomePartyServices extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    private CustomerPartyServices serviceName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_home_party_id")
    private CustomerHomeParty customerHomeParty;

    @Builder
    public CustomerHomePartyServices(final CustomerPartyServices serviceName, final CustomerHomeParty customerHomeParty) {
        this.serviceName = serviceName;
        this.customerHomeParty = customerHomeParty;
    }
}
