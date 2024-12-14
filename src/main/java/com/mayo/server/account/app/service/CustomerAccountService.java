package com.mayo.server.account.app.service;

import com.mayo.server.account.adapter.in.web.CustomerAccountEditRequest;
import com.mayo.server.account.adapter.in.web.CustomerAccountRequest;
import com.mayo.server.account.adapter.out.persistence.CustomerAccountResponse;
import com.mayo.server.account.app.port.in.CustomerAccountInputPort;
import com.mayo.server.account.domain.models.CustomerAccount;
import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerAccountService {

    private final CustomerAccountInputPort customerAccountInputPort;
    private final JwtService jwtService;

    @Transactional
    public void postAccount(final CustomerAccountRequest customerAccountRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Customer customer = customerAccountInputPort.getCustomer(userId);
        checkAccountExists(userId);

        CustomerAccount customerAccount = CustomerAccount.builder()
                .bank(customerAccountRequest.bank())
                .account(customerAccountRequest.account())
                .customer(customer)
                .build();
        customerAccountInputPort.postAccount(customerAccount);
    }

    private void checkAccountExists(final Long userId) {
        if (customerAccountInputPort.checkAccountExists(userId)) {
            throw new DuplicateException(ErrorCode.CUSTOMER_ACCOUNT_DUPLICATED);
        }
    }

    public CustomerAccountResponse getAccount(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        CustomerAccount customerAccount = customerAccountInputPort.getAccount(userId);
        return new CustomerAccountResponse(customerAccount.getBank(), customerAccount.getAccount());
    }

    @Transactional
    public void deleteAccount(final String token) {
        Long userId = jwtService.getJwtUserId(token);

        CustomerAccount customerAccount = customerAccountInputPort.getAccount(userId);
        customerAccountInputPort.deleteAccount(customerAccount);
    }

    @Transactional
    public void editAccount(final CustomerAccountEditRequest customerAccountEditRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);

        CustomerAccount customerAccount = customerAccountInputPort.getAccount(userId);
        customerAccount.edit(customerAccountEditRequest);
    }
}
