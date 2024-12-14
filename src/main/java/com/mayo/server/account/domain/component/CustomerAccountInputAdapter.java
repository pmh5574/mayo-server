package com.mayo.server.account.domain.component;

import com.mayo.server.account.app.port.in.CustomerAccountInputPort;
import com.mayo.server.account.domain.models.CustomerAccount;
import com.mayo.server.account.domain.repository.CustomerAccountRepository;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerAccountInputAdapter implements CustomerAccountInputPort {

    private final CustomerRepository customerRepository;
    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public Customer getCustomer(final Long userId) {
        return customerRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public Long postAccount(final CustomerAccount customerAccount) {
        return customerAccountRepository.save(customerAccount)
                .getCustomerAccountId();
    }

    @Override
    public CustomerAccount getAccount(final Long userId) {
        return customerAccountRepository.findByCustomerId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_ACCOUNT_NOT_FOUND));
    }

    @Override
    public boolean checkAccountExists(final Long userId) {
        return customerAccountRepository.findByCustomerId(userId)
                .isPresent();
    }

    @Override
    public void deleteAccount(final CustomerAccount customerAccount) {
        customerAccountRepository.delete(customerAccount);
    }
}
