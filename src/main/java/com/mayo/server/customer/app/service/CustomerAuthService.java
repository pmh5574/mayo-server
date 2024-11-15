package com.mayo.server.customer.app.service;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.utility.PwdUtility;
import com.mayo.server.customer.adapter.in.web.CustomerEmailRegisterRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLoginRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLogoutRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPasswordChange;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneRegisterRequest;
import com.mayo.server.customer.adapter.in.web.ReissueAccessToken;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerAuthService {

    private final JwtService jwtService;
    private final CustomerQueryInputPort customerQueryInputPort;
    private final CustomerPhoneQueryInputPort customerPhoneQueryInputPort;
    private final CustomerEmailQueryInputPort customerEmailQueryInputPort;
    private final CustomerVerifyService customerVerifyService;

    @Transactional
    public JwtToken login(CustomerLoginRequest customerLoginRequest) {
        String hashedPwd = PwdUtility.hash(customerLoginRequest.password());
        Long id = getUserIdByUserType(customerLoginRequest, hashedPwd);

        return jwtService.createJwtToken(id, UserType.CUSTOMER);
    }

    private Long getUserIdByUserType(CustomerLoginRequest customerLoginRequest, String hashedPwd) {
        return customerQueryInputPort.findByCustomerUsernameAndCustomerPassword(customerLoginRequest.username(), hashedPwd).getId();
    }

    @Transactional
    public void logout(CustomerLogoutRequest customerLogoutRequest) {
        jwtService.deleteRefreshToken(customerLogoutRequest.id(), UserType.CUSTOMER);
    }

    @Transactional
    public JwtToken reissueAccessToken(ReissueAccessToken reissueAccessToken) {
        return jwtService.reissueAccessToken(reissueAccessToken.refreshToken());
    }

    @Transactional
    public void registerByPhone(CustomerPhoneRegisterRequest customerPhoneRegisterRequest) {
        duplicatedPhone(customerPhoneRegisterRequest.phone());
        duplicatedUserId(customerPhoneRegisterRequest.userId());
        validatePhoneAuthCode(customerPhoneRegisterRequest);

        customerPhoneQueryInputPort.deleteCustomerPhone(customerPhoneRegisterRequest.phone());
        customerQueryInputPort.postRegisterByPhone(customerPhoneRegisterRequest);
    }

    private void duplicatedPhone(String phone) {
        boolean phoneCheck = customerQueryInputPort.findByPhoneAndCheckRegister(phone);
        if (!phoneCheck) {
            throw new DuplicateException(ErrorCode.CUSTOMER_DUPLICATED_PHONE);
        }
    }

    private void duplicatedUserId(String userId) {
        boolean userIdCheck = customerQueryInputPort.findByUsernameAndCheckRegister(userId);
        if (!userIdCheck) {
            throw new DuplicateException(ErrorCode.CUSTOMER_DUPLICATED_USER_ID);
        }
    }

    private void validatePhoneAuthCode(CustomerPhoneRegisterRequest customerPhoneRegisterRequest) {
        boolean authCodeCheck = customerPhoneQueryInputPort.findByAuthCodeAndPhoneAndCheckRegister(
                customerPhoneRegisterRequest.authCode(),
                customerPhoneRegisterRequest.phone());
        if(!authCodeCheck) {
            throw new NotEqualException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS);
        }
    }

    @Transactional
    public void registerByEmail(CustomerEmailRegisterRequest customerEmailRegisterRequest) {
        duplicatedEmail(customerEmailRegisterRequest.email());
        duplicatedUserId(customerEmailRegisterRequest.userId());
        validateEmailAuthCode(customerEmailRegisterRequest);

        customerEmailQueryInputPort.deleteCustomerEmail(customerEmailRegisterRequest.email());
        customerQueryInputPort.postRegisterByEmail(customerEmailRegisterRequest);
    }

    private void validateEmailAuthCode(CustomerEmailRegisterRequest customerEmailRegisterRequest) {
        boolean authCodeCheck = customerEmailQueryInputPort.findByAuthCodeAndEmailAndCheckRegister(
                customerEmailRegisterRequest.authCode(),
                customerEmailRegisterRequest.email());
        if (!authCodeCheck) {
            throw new NotEqualException(ErrorCode.EMAIL_AUTH_NUMBER_NOT_EQUALS);
        }
    }

    private void duplicatedEmail(String email) {
        boolean emailCheck = customerQueryInputPort.findByEmailAndCheckRegister(email);
        if(!emailCheck) {
            throw new DuplicateException(ErrorCode.CUSTOMER_DUPLICATED_EMAIL);
        }
    }

    @Transactional
    public void patchPasswordChange(final CustomerPasswordChange customerPasswordChange) {
        checkPasswordEqual(customerPasswordChange.password(), customerPasswordChange.passwordCheck());
        Customer customer = customerQueryInputPort.findByUsername(customerPasswordChange.userId());
        customerQueryInputPort.updatePassword(customer, PwdUtility.hash(customerPasswordChange.password()));
    }

    private void checkPasswordEqual(final String password, final String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new NotEqualException(ErrorCode.PWD_NOT_EQUALS);
        }
    }
}
