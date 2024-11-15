package com.mayo.server.account.domain.repository;

import com.mayo.server.account.domain.models.CustomerAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    Optional<CustomerAccount> findByCustomerId(Long userId);
}
