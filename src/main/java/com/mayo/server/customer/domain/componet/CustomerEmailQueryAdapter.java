package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.CustomerEmail;
import com.mayo.server.customer.domain.repository.CustomerEmailRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerEmailQueryAdapter implements CustomerEmailQueryInputPort {

    private final CustomerEmailRepository customerEmailRepository;

    @Override
    public void deleteCustomerEmail(String email) {
        customerEmailRepository.findByEmail(email).ifPresent(customerEmailRepository::delete);
    }

    @Override
    public Long postCustomerEmail(String email, CustomerVerificationStatus customerVerificationStatus, String authCode) {
        return customerEmailRepository.save(CustomerEmail.builder()
                        .email(email)
                        .authCode(authCode)
                        .verificationStatus(customerVerificationStatus)
                        .build())
                .getId();
    }

    @Override
    public boolean findByAuthCodeAndEmailAndCheckRegister(String authCode, String email) {
        return customerEmailRepository.findByAuthCodeAndEmail(authCode, email).isPresent();
    }

    @Override
    public void checkFindIdByNameAndEmailAndAuthCode(final String email, final String authCode) {
        customerEmailRepository.findByAuthCodeAndEmail(authCode, email)
                .orElseThrow(() -> new NotEqualException(ErrorCode.EMAIL_AUTH_NUMBER_NOT_EQUALS));
    }

    @Override
    public void checkFindIdByEmailAndAuthCode(final String email, final String authCode) {
        customerEmailRepository.findByAuthCodeAndEmail(authCode, email)
                .orElseThrow(() -> new NotEqualException(ErrorCode.EMAIL_AUTH_NUMBER_NOT_EQUALS));
    }

    @Override
    public void checkFindIdByEmailAndAuthCodeAndStatus(final String email, final String authCode,
                                                       final CustomerVerificationStatus customerVerificationStatus) {
        customerEmailRepository.findByAuthCodeAndEmailAndVerificationStatus(authCode, email, customerVerificationStatus)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR));
    }
}
