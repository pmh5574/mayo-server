package com.mayo.server.payment.domain.models;

import com.mayo.server.party.domain.model.CustomerHomeParty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pay_home_party")
@Entity
public class PayHomeParty {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private BigDecimal budget;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_home_party_id")
    private CustomerHomeParty customerHomeParty;
}
