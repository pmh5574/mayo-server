package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.CustomerPhone;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPhoneRepository extends JpaRepository<CustomerPhone, Long> {
    Optional<CustomerPhone> findByPhoneNum(String phone);

    Optional<CustomerPhone> findByAuthCodeAndPhoneNum(String authCode, String phone);

    Optional<CustomerPhone> findByAuthCodeAndPhoneNumAndVerificationStatus(String authCode, String phone, CustomerVerificationStatus customerVerificationStatus);
}
