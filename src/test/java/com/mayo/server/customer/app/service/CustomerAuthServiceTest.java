package com.mayo.server.customer.app.service;

import static com.mayo.server.common.enums.ErrorCode.CUSTOMER_DUPLICATED_EMAIL;
import static com.mayo.server.common.enums.ErrorCode.CUSTOMER_DUPLICATED_PHONE;
import static com.mayo.server.common.enums.ErrorCode.CUSTOMER_DUPLICATED_USER_ID;
import static com.mayo.server.common.enums.ErrorCode.CUSTOMER_NOT_FOUND;
import static com.mayo.server.common.enums.ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS;
import static com.mayo.server.common.enums.ErrorCode.PWD_NOT_EQUALS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mayo.server.IntegrationTestSupport;
import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.PwdUtility;
import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerLoginServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPasswordChangeServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.CustomerEmail;
import com.mayo.server.customer.domain.model.CustomerPhone;
import com.mayo.server.customer.domain.repository.CustomerEmailRepository;
import com.mayo.server.customer.domain.repository.CustomerPhoneRepository;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class CustomerAuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private CustomerAuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerPhoneRepository customerPhoneRepository;

    @Autowired
    private CustomerEmailRepository customerEmailRepository;

    @AfterEach
    void tearDown() {
        customerEmailRepository.deleteAllInBatch();
        customerPhoneRepository.deleteAllInBatch();
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

    @DisplayName("아이디나 비밀번호가 잘못되면 회원 로그인시 예외가 발생한다.")
    @Test
    void loginWithNotFoundCustomer() {
        // given
        String id = "test";
        Customer customer1 = createCustomer(id, "", "");
        CustomerLoginServiceRequest customerLoginServiceRequest = new CustomerLoginServiceRequest(id, "qwer12345");

        // when // then
        assertThatThrownBy(() -> authService.login(customerLoginServiceRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CUSTOMER_NOT_FOUND.getMessage());
    }

    @DisplayName("휴대폰으로 회원 가입을 진행한다.")
    @Test
    void registerByPhone() {
        // given
        String phone = "01012341234";
        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                phone,
                "test"
        );

        savePhone(phone);

        // when
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

    @DisplayName("휴대폰으로 회원 가입시 중복된 휴대폰이면 예외가 발생한다.")
    @Test
    void registerByPhoneWithDuplicatedPhone() {
        // given
        String id = "test";
        String phone = "01012341234";
        Customer customer1 = createCustomer(id, phone, "");

        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                phone,
                "test"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByPhone(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_PHONE.getMessage());
    }

    @DisplayName("휴대폰으로 회원 가입시 중복된 아이디면 예외가 발생한다.")
    @Test
    void registerByPhoneWithDuplicatedUserId() {
        // given
        String id = "test";
        Customer customer1 = createCustomer(id, "", "");

        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "01012341234",
                "testCode"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByPhone(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_USER_ID.getMessage());
    }

    @DisplayName("휴대폰으로 회원 가입시 인증번호가 없으면 예외가 발생한다.")
    @Test
    void registerByPhoneWithNoPhoneAuthCode() {
        // given
        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "01012341234",
                "testCode"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByPhone(request))
                .isInstanceOf(NotEqualException.class)
                .hasMessage(PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage());
    }

    @DisplayName("휴대폰으로 회원 가입시 인증번호가 다르면 예외가 발생한다.")
    @Test
    void registerByPhoneWithValidatePhoneAuthCode() {
        // given
        String phone = "01012341234";
        CustomerPhoneRegisterServiceRequest request = new CustomerPhoneRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                phone,
                "testCode"
        );

        savePhone(phone);

        // when // then
        assertThatThrownBy(() -> authService.registerByPhone(request))
                .isInstanceOf(NotEqualException.class)
                .hasMessage(PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage());
    }

    @DisplayName("이메일로 회원 가입을 진행한다.")
    @Test
    void registerByEmail() {
        // given
        String email = "test@test.com";
        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                email,
                "test"
        );

        saveEmail(email);
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

    @DisplayName("이메일로 회원 가입시 중복된 이메일이면 예외가 발생한다.")
    @Test
    void registerByEmailWithDuplicatedPhone() {
        // given
        String password = "qwer1234";
        String id = "test";
        Customer customer1 = createCustomer(id, "", "test@test.com");

        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "test@test.com",
                "testCode"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByEmail(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_EMAIL.getMessage());
    }

    @DisplayName("이메일로 회원 가입시 중복된 아이디면 예외가 발생한다.")
    @Test
    void registerByEmailWithDuplicatedUserId() {
        // given
        String password = "qwer1234";
        String id = "test";
        Customer customer1 = createCustomer(id, "", "test@test.com");

        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "test2@test.com",
                "testCode"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByEmail(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_USER_ID.getMessage());
    }

    @DisplayName("이메일로 회원 가입시 인증번호가 없으면 예외가 발생한다.")
    @Test
    void registerByEmailWithNoEmailAuthCode() {
        // given
        String password = "qwer1234";
        String id = "test";
        Customer customer1 = createCustomer(id, "", "test@test.com");

        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                "test2@test.com",
                "testCode"
        );

        // when // then
        assertThatThrownBy(() -> authService.registerByEmail(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_USER_ID.getMessage());
    }

    @DisplayName("이메일로 회원 가입시 인증번호가 다르면 예외가 발생한다.")
    @Test
    void registerByEmailWithValidateEmailAuthCode() {
        // given
        String password = "qwer1234";
        String id = "test";
        Customer customer1 = createCustomer(id, "", "test@test.com");

        String email = "test2@test.com";
        CustomerEmailRegisterServiceRequest request = new CustomerEmailRegisterServiceRequest(
                "test",
                "qwer1234",
                "홍길동",
                "19910101",
                email,
                "testCode"
        );

        saveEmail(email);

        // when // then
        assertThatThrownBy(() -> authService.registerByEmail(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(CUSTOMER_DUPLICATED_USER_ID.getMessage());
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

    @DisplayName("회원의 비밀번호를 변경시 두 개의 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void patchPasswordChangeWithCheckPasswordEqual() {
        // given
        String id = "test";
        Customer customer1 = createCustomer(id, "", "");

        CustomerPasswordChangeServiceRequest request = new CustomerPasswordChangeServiceRequest("test", "test1234", "test12342");

        // when // then
        assertThatThrownBy(() -> authService.patchPasswordChange(request))
                .isInstanceOf(NotEqualException.class)
                .hasMessage(PWD_NOT_EQUALS.getMessage());
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

    private void savePhone(final String phone) {
        customerPhoneRepository.save(CustomerPhone.builder()
                .phoneNum(phone)
                .authCode("test")
                .verificationStatus(CustomerVerificationStatus.REGISTER)
                .build());
    }

    private void saveEmail(final String email) {
        customerEmailRepository.save(CustomerEmail.builder()
                .email(email)
                .authCode("test")
                .build());
    }
}