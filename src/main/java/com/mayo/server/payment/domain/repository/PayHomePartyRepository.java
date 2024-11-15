package com.mayo.server.payment.domain.repository;

import com.mayo.server.payment.domain.models.PayHomeParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHomePartyRepository extends JpaRepository<PayHomeParty, String> {
}
