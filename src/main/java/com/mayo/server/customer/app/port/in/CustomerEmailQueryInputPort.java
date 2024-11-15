package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;

public interface CustomerEmailQueryInputPort {

    Long postCustomerEmail(String email, CustomerVerificationStatus customerVerificationStatus, String authCode);

    boolean findByAuthCodeAndEmailAndCheckRegister(String s, String email);

    void deleteCustomerEmail(String email);

    void checkFindIdByNameAndEmailAndAuthCode(String email, String authCode);

    void checkFindIdByEmailAndAuthCode(String email, String authCode);

    void checkFindIdByEmailAndAuthCodeAndStatus(String email, String authCode, CustomerVerificationStatus customerVerificationStatus);
}
