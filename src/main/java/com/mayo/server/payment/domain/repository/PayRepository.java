package com.mayo.server.payment.domain.repository;

import com.mayo.server.payment.domain.models.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, String> {

}
