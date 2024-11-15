package com.mayo.server.customer.domain.model;

import static jakarta.persistence.FetchType.LAZY;

import com.mayo.server.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class KitchenTools extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kitchenToolsId;

    private String toolName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    public KitchenTools(final String toolName) {
        this.toolName = toolName;
    }

    public void addKitchen(final Kitchen kitchen) {
        this.kitchen = kitchen;
    }
}
