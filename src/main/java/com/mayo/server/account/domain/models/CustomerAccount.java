package com.mayo.server.account.domain.models;

import static jakarta.persistence.FetchType.LAZY;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.customer.domain.model.Customer;
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
public class CustomerAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerAccountId;

    @Enumerated(EnumType.STRING)
    private BankCode bank;

    private String account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder
    public CustomerAccount(final BankCode bank, final String account, final Customer customer) {
        this.bank = bank;
        this.account = account;
        this.customer = customer;
    }
}
