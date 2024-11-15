package com.mayo.server.customer.domain.model;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.customer.adapter.in.web.KitchenEdit;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Kitchen extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kitchenId;

    @Enumerated(EnumType.STRING)
    private KitchenMainStatus mainStatus;

    private String nickName;

    private String address;

    private String addressSub;

    private String burnerType;

    private String burnerQuantity;

    private String additionalEquipment;

    private String requirements;

    private String considerations;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "kitchen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenTools> kitchenToolsList = new ArrayList<>();

    @OneToMany(mappedBy = "kitchen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KitchenImages> kitchenImagesList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Builder
    public Kitchen(final KitchenMainStatus mainStatus, final String nickName, final String address, final String addressSub, final String burnerType,
                   final String burnerQuantity, final String additionalEquipment, final String requirements, final String considerations) {
        this.mainStatus = mainStatus;
        this.nickName = nickName;
        this.address = address;
        this.addressSub = addressSub;
        this.burnerType = burnerType;
        this.burnerQuantity = burnerQuantity;
        this.additionalEquipment = additionalEquipment;
        this.requirements = requirements;
        this.considerations = considerations;
    }

    public void addImages(final List<KitchenImages> kitchenImagesList) {
        this.kitchenImagesList.addAll(kitchenImagesList);
        kitchenImagesList.forEach(kitchenImages -> kitchenImages.addKitchen(this));
    }

    public void addTools(final List<KitchenTools> kitchenToolsList) {
        this.kitchenToolsList.addAll(kitchenToolsList);
        kitchenToolsList.forEach(kitchenTools -> kitchenTools.addKitchen(this));
    }

    public void addCustomer(final Customer customer) {
        this.customer = customer;
    }

    public void edit(final KitchenEdit kitchenEdit) {
        if (kitchenEdit.nickName() != null) {
            this.nickName = kitchenEdit.nickName();
        }

        if (kitchenEdit.address() != null) {
            this.address = kitchenEdit.address();
        }

        if (kitchenEdit.addressSub() != null) {
            this.addressSub = kitchenEdit.addressSub();
        }

        if (kitchenEdit.burnerType() != null) {
            this.burnerType = kitchenEdit.burnerType();
        }

        if (kitchenEdit.burnerQuantity() != null) {
            this.burnerQuantity = kitchenEdit.burnerQuantity();
        }

        this.additionalEquipment = kitchenEdit.additionalEquipment();
        this.requirements = kitchenEdit.requirements();
        this.considerations = kitchenEdit.considerations();

    }

    public void patchMain(final KitchenMainStatus kitchenMainStatus) {
        this.mainStatus = kitchenMainStatus;
    }
}
