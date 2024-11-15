package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.domain.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerUsernameAndCustomerPassword(String username, String password);

    Optional<Customer> findByCustomerPhone(String phone);

    Optional<Customer> findByCustomerUsername(String username);

    Optional<Customer> findByCustomerEmail(String email);

    Optional<Customer> findByCustomerPhoneAndCustomerName(String phone, String name);

    Optional<Customer> findByCustomerEmailAndCustomerName(String email, String name);

    Optional<Customer> findByCustomerUsernameAndCustomerPhoneAndCustomerNameAndCustomerBirthday(String username, String phone, String name,
                                                                                                final String birthday);

    Optional<Customer> findByCustomerUsernameAndCustomerPhone(String username, String phone);

    Optional<Customer> findByCustomerUsernameAndCustomerEmailAndCustomerNameAndCustomerBirthday(String username, String email, String name, String birthday);

    Optional<Customer> findByCustomerUsernameAndCustomerEmail(String username, String email);
}
