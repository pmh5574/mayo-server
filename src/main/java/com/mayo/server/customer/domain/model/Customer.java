package com.mayo.server.customer.domain.model;

import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.utility.PwdUtility;
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
    private Customer(String customerUsername, String customerPassword, String customerName, String customerBirthday,
                    String customerPhone, String customerEmail) {
        this.customerUsername = validateUserId(customerUsername);
        this.customerPassword = PwdUtility.hash(validatePassword(customerPassword));
        this.customerName = customerName;
        this.customerBirthday = validateBirthday(customerBirthday);
        this.customerPhone = validatePhone(customerPhone);
        this.customerEmail = validateEmail(customerEmail);
    }

    public void changePassword(final String password) {
        this.customerPassword = PwdUtility.hash(validatePassword(password));
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

    private String validateUserId(final String userId) {
        if (!userId.matches("^[a-zA-Z\\d]{4,20}$")) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_VALID_USER_ID);
        }
        return userId;
    }

    private String validatePassword(final String password) {
        if (!password.matches("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$")) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_VALID_PASSWORD);
        }
        return password;
    }

    private String validateBirthday(final String birthday) {
        if (!birthday.matches("^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$")) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_VALID_BIRTHDAY);
        }
        return birthday;
    }

    private String validatePhone(final String phone) {

        if (phone == null || phone.isEmpty()) {
            return null;
        }

        if (!phone.matches("^(?!.*-)[0-9]{10,11}$")) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_VALID_PHONE);

        }
        return phone;
    }

    private String validateEmail(final String email) {

        if (email == null || email.isEmpty()) {
            return null;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_VALID_EMAIL);
        }
        return email;
    }
}
