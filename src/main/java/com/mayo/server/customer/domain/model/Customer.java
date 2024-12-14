package com.mayo.server.customer.domain.model;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.customer.adapter.in.web.CustomerEdit;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartySchedule;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Customer extends BaseTimeEntity {

    @Id
    @Tsid
    private Long id;

    private String customerUsername;

    private String customerPassword;

    private String customerName;

    private String customerBirthday;

    private String customerPhone;

    private String customerEmail;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerHomeParty> customerHomeParties = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Kitchen> customerKitchen = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<PartySchedule>  partySchedules = new ArrayList<>();

    @Builder
    public Customer(String customerUsername, String customerPassword, String customerName, String customerBirthday,
                    String customerPhone, String customerEmail) {
        this.customerUsername = customerUsername;
        this.customerPassword = customerPassword;
        this.customerName = customerName;
        this.customerBirthday = customerBirthday;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
    }

    public void changePassword(final String password) {
        this.customerPassword = password;
    }

    public void addKitchen(final Kitchen kitchen) {
        customerKitchen.add(kitchen);
        kitchen.addCustomer(this);
    }

    public void edit(final CustomerEdit customerEdit, final boolean phoneChangeCheck, final boolean emailChangeCheck) {
        this.customerName = customerEdit.name();
        this.customerBirthday = customerEdit.birthday();

        if (phoneChangeCheck) {
            this.customerPhone = customerEdit.phone();
        }

        if (emailChangeCheck) {
            this.customerEmail = customerEdit.email();
        }
    }
}
