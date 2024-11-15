package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.CustomerPhone;
import com.mayo.server.customer.domain.repository.CustomerPhoneRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerPhoneQueryAdapter implements CustomerPhoneQueryInputPort {

    private final CustomerPhoneRepository customerPhoneRepository;

    @Override
    public Long postCustomerPhone(String phone, CustomerVerificationStatus customerVerificationStatus, String authCode) {
        return customerPhoneRepository.save(CustomerPhone.builder()
                        .phoneNum(phone)
                        .authCode(authCode)
                        .verificationStatus(customerVerificationStatus)
                        .build())
                .getId();
    }

    @Override
    public void deleteCustomerPhone(String phone) {
        customerPhoneRepository.findByPhoneNum(phone)
                .ifPresent(customerPhoneRepository::delete);
    }

    @Override
    public boolean findByAuthCodeAndPhoneAndCheckRegister(String authCode, String phone) {
        return customerPhoneRepository.findByAuthCodeAndPhoneNum(authCode, phone).isPresent();
    }

    @Override
    public void checkFindIdByPhoneAndAuthCode(final String phone, final String authCode) {
        customerPhoneRepository.findByAuthCodeAndPhoneNum(authCode, phone)
                .orElseThrow(() -> new NotEqualException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS));
    }

    @Override
    public void checkFindIdByPhoneAndAuthCodeAndStatus(final String phone, final String authCode,
                                                       final CustomerVerificationStatus customerVerificationStatus) {
        customerPhoneRepository.findByAuthCodeAndPhoneNumAndVerificationStatus(authCode, phone, customerVerificationStatus)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR));
    }
}
