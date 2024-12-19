package com.mayo.server.customer.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mayo.server.IntegrationTestSupport;
import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.common.utility.PwdUtility;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerLoginServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPasswordChangeServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
class CustomerAuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private CustomerAuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private CustomerPhoneQueryInputPort customerPhoneQueryInputPort;

    @MockBean
    private CustomerEmailQueryInputPort customerEmailQueryInputPort;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
    }

    @DisplayName("회원 로그인을 진행한다.")
    @Test
    void login() {
        // given
        String password = "qwer1234";
        String id = "test";
        Customer customer1 = createCustomer(id, "", "");
        CustomerLoginServiceRequest customerLoginServiceRequest = new CustomerLoginServiceRequest(id, password);

        // when
        JwtToken jwtToken = authService.login(customerLoginServiceRequest);

        // then
        assertThat(jwtToken).isNotNull();
    }

    @DisplayName("휴대폰으로 인증된 회원 가입을 진행한다.")
    @Test
    void registerByPhone() {
        // given
        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "01012341234",
                "testCode"
        );

        // when
        when(customerPhoneQueryInputPort.findByAuthCodeAndPhoneAndCheckRegister(
                "testCode", "01012341234"))
                .thenReturn(true);
        authService.registerByPhone(request);

        // then
        Customer customer = customerRepository.findAll().get(0);
        assertThat(customer).isNotNull();
        assertThat(customer).extracting("customerUsername", "customerName", "customerBirthday", "customerPhone")
                .contains(
                        "test",
                        "홍길동",
                        "19910101",
                        "01012341234");
    }

    @DisplayName("이메일로 인증된 회원 가입을 진행한다.")
    @Test
    void registerByEmail() {
        // given
        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "test@test.com",
                "testCode"
        );

        // when
        when(customerEmailQueryInputPort.findByAuthCodeAndEmailAndCheckRegister(
                "testCode", "test@test.com"))
                .thenReturn(true);
        authService.registerByEmail(request);

        // then
        Customer customer = customerRepository.findAll().get(0);
        assertThat(customer).isNotNull();
        assertThat(customer).extracting("customerUsername", "customerName", "customerBirthday", "customerEmail")
                .contains(
                        "test",
                        "홍길동",
                        "19910101",
                        "test@test.com");
    }

    @DisplayName("회원의 비밀번호를 변경한다.")
    @Test
    void patchPasswordChange() {
        // given
        String id = "test";
        Customer customer1 = createCustomer(id, "", "");

        CustomerPasswordChangeServiceRequest request = new CustomerPasswordChangeServiceRequest("test", "test1234", "test1234");

        // when
        authService.patchPasswordChange(request);

        // then
        Optional<Customer> customer = customerRepository.findByCustomerUsernameAndCustomerPassword(
                "test",
                PwdUtility.hash("test1234")
        );
        assertThat(customer).isPresent();
    }

    private Customer createCustomer(final String userName,
                                    final String phone,
                                    final String email) {
        Customer customer = Customer.builder()
                .customerUsername(userName)
                .customerName("홍길동")
                .customerPhone(phone)
                .customerEmail(email)
                .customerPassword("qwer1234")
                .customerBirthday("19910101")
                .build();
        return customerRepository.save(customer);
    }
}