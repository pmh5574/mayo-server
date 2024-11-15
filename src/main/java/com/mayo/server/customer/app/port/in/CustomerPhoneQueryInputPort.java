package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;

public interface CustomerPhoneQueryInputPort {
    Long postCustomerPhone(String phone, CustomerVerificationStatus customerVerificationStatus, String authCode);

    void deleteCustomerPhone(String phone);

    boolean findByAuthCodeAndPhoneAndCheckRegister(String authCode, String phone);

    void checkFindIdByPhoneAndAuthCode(String phone, String authCode);

    void checkFindIdByPhoneAndAuthCodeAndStatus(String phone, String authCode, CustomerVerificationStatus customerVerificationStatus);
}
