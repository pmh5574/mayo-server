package com.mayo.server.customer.app.service;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.utility.PwdUtility;
import com.mayo.server.customer.adapter.in.web.CustomerLogoutRequest;
import com.mayo.server.customer.adapter.in.web.ReissueAccessToken;
import com.mayo.server.customer.app.port.in.CustomerAuthUseCase;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerLoginServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPasswordChangeServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import com.mayo.server.customer.domain.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerAuthService implements CustomerAuthUseCase {

    private final JwtService jwtService;
    private final CustomerQueryInputPort customerQueryInputPort;
    private final CustomerPhoneQueryInputPort customerPhoneQueryInputPort;
    private final CustomerEmailQueryInputPort customerEmailQueryInputPort;
    private final CustomerVerifyService customerVerifyService;

    @Transactional
    public JwtToken login(CustomerLoginServiceRequest request) {
        String hashedPwd = PwdUtility.hash(request.password());
        Long id = getUserIdByUserType(request, hashedPwd);

        return jwtService.createJwtToken(id, UserType.CUSTOMER);
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
    public void registerByPhone(CustomerPhoneRegisterServiceRequest request) {
        duplicatedPhone(request.phone());
        duplicatedUserId(request.userId());
        validatePhoneAuthCode(request);

        customerPhoneQueryInputPort.deleteCustomerPhone(request.phone());
        customerQueryInputPort.postRegisterByPhone(request);
    }

    @Transactional
    public void registerByEmail(CustomerEmailRegisterServiceRequest request) {
        duplicatedEmail(request.email());
        duplicatedUserId(request.userId());
        validateEmailAuthCode(request);

        customerEmailQueryInputPort.deleteCustomerEmail(request.email());
        customerQueryInputPort.postRegisterByEmail(request);
    }

    @Transactional
    public void patchPasswordChange(final CustomerPasswordChangeServiceRequest request) {
        checkPasswordEqual(request.password(), request.passwordCheck());
        Customer customer = customerQueryInputPort.findByUsername(request.userId());
        customerQueryInputPort.updatePassword(customer, request.password());
    }

    private Long getUserIdByUserType(CustomerLoginServiceRequest request, String hashedPwd) {
        return customerQueryInputPort.findByCustomerUsernameAndCustomerPassword(request.username(), hashedPwd).getId();
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

    private void validatePhoneAuthCode(CustomerPhoneRegisterServiceRequest request) {
        boolean authCodeCheck = customerPhoneQueryInputPort.findByAuthCodeAndPhoneAndCheckRegister(
                request.authCode(),
                request.phone());
        if(!authCodeCheck) {
            throw new NotEqualException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS);
        }
    }

    private void validateEmailAuthCode(CustomerEmailRegisterServiceRequest request) {
        boolean authCodeCheck = customerEmailQueryInputPort.findByAuthCodeAndEmailAndCheckRegister(
                request.authCode(),
                request.email());
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

    private void checkPasswordEqual(final String password, final String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new NotEqualException(ErrorCode.PWD_NOT_EQUALS);
        }
    }
}
