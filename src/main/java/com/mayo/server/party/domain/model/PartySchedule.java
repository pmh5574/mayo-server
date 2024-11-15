package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.customer.domain.model.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table(name = "customer_home_party_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PartySchedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer isMatched;

    private String createdAt;

    private String deletedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chef_id")
    private Chef chef;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_home_party_id")
    private CustomerHomeParty customerHomeParty;

    public void customerMatched() {
        this.isMatched = 1;
    }
}
