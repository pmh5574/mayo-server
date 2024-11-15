package com.mayo.server.account.domain.repository;

import com.mayo.server.account.domain.models.ChefAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefAccountRepository extends JpaRepository<ChefAccount, Long> {

    ChefAccount findByChefId(Long chefId);
}
