package com.mayo.server.customer.app.service;

import com.mayo.server.IntegrationTestSupport;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomerAuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private CustomerAuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
    }

    @DisplayName("회원 로그인을 진행한다.")
    @Test
    void login() {
        // given


        // when


        // then
    }
}