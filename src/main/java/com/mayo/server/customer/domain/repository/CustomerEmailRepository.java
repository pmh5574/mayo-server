package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.CustomerEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerEmailRepository extends JpaRepository<CustomerEmail, Long> {
    Optional<CustomerEmail> findByEmail(String email);

    Optional<CustomerEmail> findByAuthCodeAndEmail(String authCode, String email);

    Optional<CustomerEmail> findByAuthCodeAndEmailAndVerificationStatus(String authCode, String email, CustomerVerificationStatus customerVerificationStatus);
}
