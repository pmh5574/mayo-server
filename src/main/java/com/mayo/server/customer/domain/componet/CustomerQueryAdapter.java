package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerQueryAdapter implements CustomerQueryInputPort {

    private final CustomerRepository customerRepository;

    @Override
    public Customer findByCustomerUsernameAndCustomerPassword(String username, String password) {
        return customerRepository.findByCustomerUsernameAndCustomerPassword(username, password)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public boolean findByPhoneAndCheckRegister(String phone) {
        return customerRepository.findByCustomerPhone(phone).isEmpty();
    }

    @Override
    public boolean findByUsernameAndCheckRegister(String userId) {
        return customerRepository.findByCustomerUsername(userId).isEmpty();
    }

    @Override
    public Long postRegisterByPhone(CustomerPhoneRegisterServiceRequest request) {
        return customerRepository.save(Customer.builder()
                        .customerUsername(request.userId())
                        .customerPassword(request.password())
                        .customerName(request.name())
                        .customerBirthday(request.birthday())
                        .customerPhone(request.phone())
                .build())
                .getId();
    }

    @Override
    public boolean findByEmailAndCheckRegister(String email) {
        return customerRepository.findByCustomerEmail(email).isEmpty();
    }

    @Override
    public Long postRegisterByEmail(CustomerEmailRegisterServiceRequest request) {
        return customerRepository.save(Customer.builder()
                        .customerUsername(request.userId())
                        .customerPassword(request.password())
                        .customerName(request.name())
                        .customerBirthday(request.birthday())
                        .customerEmail(request.email())
                        .build())
                .getId();
    }

    @Override
    public void checkIdFindByPhone(final String phone) {
        customerRepository.findByCustomerPhone(phone)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_VERIFY_USERNAME_PHONE_NOT_FOUND));
    }

    @Override
    public Customer checkIdFindByPhoneAndName(final String phone, final String name) {
        return customerRepository.findByCustomerPhoneAndCustomerName(phone, name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PHONE_NOT_FOUND));
    }

    @Override
    public void checkIdFindByEmail(final String email) {
        customerRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_VERIFY_USERNAME_EMAIL_NOT_FOUND));
    }

    @Override
    public Customer checkIdFindByEmailAndName(final String email, final String name) {
        return customerRepository.findByCustomerEmailAndCustomerName(email, name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_EMAIL_NOT_FOUND));
    }

    @Override
    public Customer checkPasswordFindByUserIdAndPhoneAndName(final String username, final String phone,
                                                             final String name, final String birthday) {
        return customerRepository.findByCustomerUsernameAndCustomerPhoneAndCustomerNameAndCustomerBirthday(username, phone, name, birthday)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PHONE_NOT_FOUND));
    }

    @Override
    public Customer checkPasswordFindByUserIdAndPhone(final String username, final String phone) {
        return customerRepository.findByCustomerUsernameAndCustomerPhone(username, phone)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR));
    }

    @Override
    public void updatePassword(final Customer customer, final String password) {
        customer.changePassword(password);
    }

    @Override
    public Customer findByUsername(final String username) {
        return customerRepository.findByCustomerUsername(username)
                .orElseThrow(IllegalAccessError::new);
    }

    @Override
    public Customer checkPasswordFindByUserIdAndEmailAndName(final String username, final String email,
                                                             final String name,
                                                             final String birthday) {
        return customerRepository.findByCustomerUsernameAndCustomerEmailAndCustomerNameAndCustomerBirthday(username, email, name, birthday)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_EMAIL_NOT_FOUND));
    }

    @Override
    public Customer checkPasswordFindByUserIdAndEmail(final String username, final String email) {
        return customerRepository.findByCustomerUsernameAndCustomerEmail(username, email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR));
    }

    @Override
    public Customer findById(final Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }
}
