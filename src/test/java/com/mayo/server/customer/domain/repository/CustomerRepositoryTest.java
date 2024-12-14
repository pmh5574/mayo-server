package com.mayo.server.customer.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mayo.server.IntegrationTestSupport;
import com.mayo.server.common.utility.PwdUtility;
import com.mayo.server.customer.domain.model.Customer;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomerRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAllInBatch();
    }

    @DisplayName("회원 아이디랑 비밀번호로 회원을 조회한다.")
    @Test
    void findByCustomerUsernameAndCustomerPassword() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String id = "test";
        Customer customer1 = createCustomer(id, password, "", "", "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsernameAndCustomerPassword(id, password);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
    }

    @DisplayName("휴대폰 번호로 회원을 조회한다.")
    @Test
    void findByCustomerPhone() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String phone = "01012341234";
        Customer customer1 = createCustomer("test", password, phone, "", "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerPhone(phone);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerPhone()).isEqualTo(phone);
    }

    @DisplayName("아이디로 회원을 조회한다.")
    @Test
    void findByCustomerUsername() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String id = "test";
        Customer customer1 = createCustomer(id, password, "", "", "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsername(id);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
    }

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void findByCustomerEmail() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String email = "test@test.com";
        Customer customer1 = createCustomer("test", password, "", email, "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerEmail(email);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerEmail()).isEqualTo(email);
    }

    @DisplayName("휴대폰 번호와 이름으로 회원을 조회한다.")
    @Test
    void findByCustomerPhoneAndCustomerName() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String phone = "01012341234";
        String name = "홍기르동";
        Customer customer1 = createCustomer("test", password, phone, "", name, "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerPhoneAndCustomerName(phone, name);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerPhone()).isEqualTo(phone);
        assertThat(customer.getCustomerName()).isEqualTo(name);
    }

    @DisplayName("이메일과 이름으로 회원을 조회한다.")
    @Test
    void findByCustomerEmailAndCustomerName() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String email = "test@test.com";
        String name = "홍기르동";
        Customer customer1 = createCustomer("test", password, "", email, name, "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerEmailAndCustomerName(email, name);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerEmail()).isEqualTo(email);
        assertThat(customer.getCustomerName()).isEqualTo(name);
    }

    @DisplayName("아이디, 휴대폰 번호, 이름, 생년월일로 회원을 조회한다.")
    @Test
    void findByCustomerUsernameAndCustomerPhoneAndCustomerNameAndCustomerBirthday() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String phone = "01012341234";
        String name = "홍기르동";
        String id = "test";
        String birthday = "19910101";
        Customer customer1 = createCustomer(id, password, phone, "", name, birthday);

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsernameAndCustomerPhoneAndCustomerNameAndCustomerBirthday(
                id,
                phone,
                name,
                birthday);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
        assertThat(customer.getCustomerPhone()).isEqualTo(phone);
        assertThat(customer.getCustomerName()).isEqualTo(name);
        assertThat(customer.getCustomerBirthday()).isEqualTo(birthday);
    }

    @DisplayName("아이디, 이메일, 이름, 생년월일로 회원을 조회한다.")
    @Test
    void findByCustomerUsernameAndCustomerEmailAndCustomerNameAndCustomerBirthday() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String email = "test@test.com";
        String name = "홍기르동";
        String id = "test";
        String birthday = "19910101";
        Customer customer1 = createCustomer(id, password, "", email, name, birthday);

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsernameAndCustomerEmailAndCustomerNameAndCustomerBirthday(
                id,
                email,
                name,
                birthday);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
        assertThat(customer.getCustomerEmail()).isEqualTo(email);
        assertThat(customer.getCustomerName()).isEqualTo(name);
        assertThat(customer.getCustomerBirthday()).isEqualTo(birthday);
    }

    @DisplayName("아이디, 휴대폰 번호로 회원을 조회한다.")
    @Test
    void findByCustomerUsernameAndCustomerPhone() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String id = "test";
        String phone = "01012341234";
        Customer customer1 = createCustomer(id, password, phone, "", "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsernameAndCustomerPhone(id, phone);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
        assertThat(customer.getCustomerPhone()).isEqualTo(phone);
    }

    @DisplayName("아이디, 이메일로 회원을 조회한다.")
    @Test
    void findByCustomerUsernameAndCustomerEmail() {
        // given
        String password = PwdUtility.hash("qwer1234");
        String id = "test";
        String email = "test@test.com";
        Customer customer1 = createCustomer(id, password, "", email, "", "");

        // when
        Optional<Customer> optionalCustomer = customerRepository.findByCustomerUsernameAndCustomerEmail(id, email);

        // then
        assertThat(optionalCustomer).isPresent();
        Customer customer = optionalCustomer.get();
        assertThat(customer.getCustomerUsername()).isEqualTo(id);
        assertThat(customer.getCustomerEmail()).isEqualTo(email);
    }


    private Customer createCustomer(final String userName,
                                    final String password,
                                    final String phone,
                                    final String email,
                                    final String name, final String birthday) {
        Customer customer = Customer.builder()
                .customerUsername(userName)
                .customerName(name)
                .customerPhone(phone)
                .customerEmail(email)
                .customerPassword(password)
                .customerBirthday(birthday)
                .build();
        return customerRepository.save(customer);
    }
}