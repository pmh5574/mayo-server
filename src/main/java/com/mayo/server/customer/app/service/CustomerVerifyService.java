package com.mayo.server.customer.app.service;

import static com.mayo.server.common.enums.UserType.CUSTOMER;

import com.mayo.server.common.enums.EmailTemplate;
import com.mayo.server.common.enums.PhoneCode;
import com.mayo.server.common.utility.GenerateUtility;
import com.mayo.server.customer.adapter.in.web.CustomerEmailVerifyEditRequest;
import com.mayo.server.customer.adapter.in.web.CustomerEmailVerifyRegisterRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneVerifyEditRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneVerifyRegisterRequest;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByEmail;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByEmailCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByPhone;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByPhoneCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByEmail;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByEmailCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByPhone;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByPhoneCheck;
import com.mayo.server.customer.adapter.in.web.VerifyEmailAndCodeAndUserTypeForPasswordChange;
import com.mayo.server.customer.adapter.in.web.VerifyPhoneAndCodeAndUserTypeForPasswordChange;
import com.mayo.server.customer.adapter.out.persistence.CustomerVerifyIdByEmail;
import com.mayo.server.customer.adapter.out.persistence.CustomerVerifyIdByPhone;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.infra.CustomerAwsSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerVerifyService {

    private final CustomerPhoneQueryInputPort customerPhoneQueryInputPort;
    private final CustomerEmailQueryInputPort customerEmailQueryInputPort;
    private final CustomerAwsSendService customerAwsSendService;
    private final CustomerQueryInputPort customerQueryInputPort;

    @Transactional
    public void registerByPhone(CustomerPhoneVerifyRegisterRequest customerPhoneVerifyRegisterRequest) {
        String authCode = getAuthCode();

        customerPhoneQueryInputPort.deleteCustomerPhone(customerPhoneVerifyRegisterRequest.phone());
        Long phoneId = postCustomerPhone(customerPhoneVerifyRegisterRequest.phone(), authCode, CustomerVerificationStatus.REGISTER);
        snsSend(CustomerVerificationStatus.REGISTER.getVerificationMessage(), authCode, customerPhoneVerifyRegisterRequest.phone());
    }

    private String getAuthCode() {
        return GenerateUtility.generateRandomCode();
    }

    private Long postCustomerPhone(String phone, String authCode, CustomerVerificationStatus customerVerificationStatus) {
        return customerPhoneQueryInputPort.postCustomerPhone(
                phone,
                customerVerificationStatus,
                authCode);
    }

    private void snsSend(String message, String authCode, String phone) {
        customerAwsSendService.snsSend(String.format(message, authCode), PhoneCode.KOREA.getCodeNum() + phone);
    }

    @Transactional
    public void registerByEmail(CustomerEmailVerifyRegisterRequest customerEmailVerifyRegisterRequest) {
        String authCode = getAuthCode();

        customerEmailQueryInputPort.deleteCustomerEmail(customerEmailVerifyRegisterRequest.email());
        Long emailId = postCustomerEmail(customerEmailVerifyRegisterRequest.email(), authCode, CustomerVerificationStatus.REGISTER);
        emailSend(CustomerVerificationStatus.REGISTER.getVerificationMessage(), authCode, customerEmailVerifyRegisterRequest.email());
    }

    private void emailSend(String message, String authCode, String email) {
        customerAwsSendService.emailSend(EmailTemplate.REGISTER_SUBJECT.getText(), String.format(message, authCode), email);
    }

    private Long postCustomerEmail(String email, String authCode, CustomerVerificationStatus customerVerificationStatus) {
        return customerEmailQueryInputPort.postCustomerEmail(
                email,
                customerVerificationStatus,
                authCode);
    }

    @Transactional
    public void findIdByPhone(final VerifyCustomerIdByPhone verifyCustomerIdByPhone) {
        String authCode = getAuthCode();

        customerQueryInputPort.checkIdFindByPhone(verifyCustomerIdByPhone.phone());
        customerPhoneQueryInputPort.deleteCustomerPhone(verifyCustomerIdByPhone.phone());
        Long phoneId = postCustomerPhone(verifyCustomerIdByPhone.phone(), authCode, CustomerVerificationStatus.USERNAME);

        snsSend(CustomerVerificationStatus.USERNAME.getVerificationMessage(), authCode, verifyCustomerIdByPhone.phone());
    }

    @Transactional
    public CustomerVerifyIdByPhone findIdByPhoneCheck(final VerifyCustomerIdByPhoneCheck verifyCustomerIdByPhoneCheck) {
        Customer customer = customerQueryInputPort.checkIdFindByPhoneAndName(verifyCustomerIdByPhoneCheck.phone(),
                verifyCustomerIdByPhoneCheck.name());
        customerPhoneQueryInputPort.checkFindIdByPhoneAndAuthCode(verifyCustomerIdByPhoneCheck.phone(), verifyCustomerIdByPhoneCheck.authCode());
        customerPhoneQueryInputPort.deleteCustomerPhone(verifyCustomerIdByPhoneCheck.phone());

        return new CustomerVerifyIdByPhone(customer.getCustomerUsername(), CUSTOMER, customer.getCreatedAt());
    }

    @Transactional
    public void findIdByEmail(final VerifyCustomerIdByEmail verifyCustomerIdByEmail) {
        String authCode = getAuthCode();

        customerQueryInputPort.checkIdFindByEmail(verifyCustomerIdByEmail.email());
        customerEmailQueryInputPort.deleteCustomerEmail(verifyCustomerIdByEmail.email());
        Long emailId = postCustomerEmail(verifyCustomerIdByEmail.email(), authCode, CustomerVerificationStatus.USERNAME);
        emailSend(CustomerVerificationStatus.USERNAME.getVerificationMessage(), authCode, verifyCustomerIdByEmail.email());
    }

    @Transactional
    public CustomerVerifyIdByEmail findIdByEmailCheck(final VerifyCustomerIdByEmailCheck verifyCustomerIdByEmailCheck) {
        Customer customer = customerQueryInputPort.checkIdFindByEmailAndName(verifyCustomerIdByEmailCheck.email(),
                verifyCustomerIdByEmailCheck.name());
        customerEmailQueryInputPort.checkFindIdByNameAndEmailAndAuthCode(verifyCustomerIdByEmailCheck.email(), verifyCustomerIdByEmailCheck.authCode());
        customerEmailQueryInputPort.deleteCustomerEmail(verifyCustomerIdByEmailCheck.email());

        return new CustomerVerifyIdByEmail(customer.getCustomerUsername(), CUSTOMER, customer.getCreatedAt());
    }

    @Transactional
    public void findPasswordByPhone(final VerifyCustomerPasswordByPhone verifyCustomerPasswordByPhone) {
        String authCode = getAuthCode();

        customerQueryInputPort.checkIdFindByPhone(verifyCustomerPasswordByPhone.phone());
        customerPhoneQueryInputPort.deleteCustomerPhone(verifyCustomerPasswordByPhone.phone());
        Long phoneId = postCustomerPhone(verifyCustomerPasswordByPhone.phone(), authCode, CustomerVerificationStatus.PWD);

        snsSend(CustomerVerificationStatus.PWD.getVerificationMessage(), authCode, verifyCustomerPasswordByPhone.phone());
    }

    @Transactional
    public void findPasswordByPhoneCheck(final VerifyCustomerPasswordByPhoneCheck verifyCustomerPasswordByPhoneCheck) {
        customerQueryInputPort.checkPasswordFindByUserIdAndPhoneAndName(
                verifyCustomerPasswordByPhoneCheck.userId(),
                verifyCustomerPasswordByPhoneCheck.phone(),
                verifyCustomerPasswordByPhoneCheck.name(),
                verifyCustomerPasswordByPhoneCheck.birthday());
        customerPhoneQueryInputPort.checkFindIdByPhoneAndAuthCode(verifyCustomerPasswordByPhoneCheck.phone(), verifyCustomerPasswordByPhoneCheck.authCode());
    }

    @Transactional
    public void verifyPhoneAndCodeAndUserTypeForPasswordChange(final VerifyPhoneAndCodeAndUserTypeForPasswordChange verifyPhoneAndCodeAndUserTypeForPasswordChange) {
        customerPhoneQueryInputPort.checkFindIdByPhoneAndAuthCodeAndStatus(verifyPhoneAndCodeAndUserTypeForPasswordChange.phone(), verifyPhoneAndCodeAndUserTypeForPasswordChange.authCode(), CustomerVerificationStatus.PWD);
        customerQueryInputPort.checkPasswordFindByUserIdAndPhone(verifyPhoneAndCodeAndUserTypeForPasswordChange.userId(), verifyPhoneAndCodeAndUserTypeForPasswordChange.phone());
    }

    @Transactional
    public void findPasswordByEmail(final VerifyCustomerPasswordByEmail verifyCustomerPasswordByEmail) {
        String authCode = getAuthCode();

        customerQueryInputPort.checkIdFindByEmail(verifyCustomerPasswordByEmail.email());
        customerEmailQueryInputPort.deleteCustomerEmail(verifyCustomerPasswordByEmail.email());
        Long emailId = postCustomerEmail(verifyCustomerPasswordByEmail.email(), authCode, CustomerVerificationStatus.PWD);
        emailSend(CustomerVerificationStatus.PWD.getVerificationMessage(), authCode, verifyCustomerPasswordByEmail.email());
    }

    @Transactional
    public void findPasswordByEmailCheck(final VerifyCustomerPasswordByEmailCheck verifyCustomerPasswordByEmailCheck) {
        customerQueryInputPort.checkPasswordFindByUserIdAndEmailAndName(
                verifyCustomerPasswordByEmailCheck.userId(),
                verifyCustomerPasswordByEmailCheck.email(),
                verifyCustomerPasswordByEmailCheck.name(),
                verifyCustomerPasswordByEmailCheck.birthday());
        customerEmailQueryInputPort.checkFindIdByEmailAndAuthCode(verifyCustomerPasswordByEmailCheck.email(), verifyCustomerPasswordByEmailCheck.authCode());
    }

    @Transactional
    public void verifyEmailAndCodeAndUserTypeForPasswordChange(final VerifyEmailAndCodeAndUserTypeForPasswordChange verifyEmailAndCodeAndUserTypeForPasswordChange) {
        customerEmailQueryInputPort.checkFindIdByEmailAndAuthCodeAndStatus(verifyEmailAndCodeAndUserTypeForPasswordChange.email(), verifyEmailAndCodeAndUserTypeForPasswordChange.authCode(), CustomerVerificationStatus.PWD);
        customerQueryInputPort.checkPasswordFindByUserIdAndEmail(verifyEmailAndCodeAndUserTypeForPasswordChange.userId(), verifyEmailAndCodeAndUserTypeForPasswordChange.email());
    }

    @Transactional
    public void editByPhoneSend(final CustomerPhoneVerifyEditRequest customerPhoneVerifyEditRequest) {
        String authCode = getAuthCode();

        customerPhoneQueryInputPort.deleteCustomerPhone(customerPhoneVerifyEditRequest.phone());
        Long phoneId = postCustomerPhone(customerPhoneVerifyEditRequest.phone(), authCode, CustomerVerificationStatus.EDIT);
        snsSend(CustomerVerificationStatus.EDIT.getVerificationMessage(), authCode, customerPhoneVerifyEditRequest.phone());
    }

    @Transactional
    public void editByEmailSend(final CustomerEmailVerifyEditRequest customerEmailVerifyEditRequest) {
        String authCode = getAuthCode();

        customerEmailQueryInputPort.deleteCustomerEmail(customerEmailVerifyEditRequest.email());
        Long emailId = postCustomerEmail(customerEmailVerifyEditRequest.email(), authCode, CustomerVerificationStatus.EDIT);
        emailSend(CustomerVerificationStatus.EDIT.getVerificationMessage(), authCode, customerEmailVerifyEditRequest.email());
    }
}
