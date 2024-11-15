package com.mayo.server.chef.app.service;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.app.service.JwtTokenProvider;
import com.mayo.server.chef.adapter.in.web.EmailRequest;
import com.mayo.server.chef.adapter.in.web.EmailVerifyRequest;
import com.mayo.server.chef.adapter.in.web.PhoneRequest;
import com.mayo.server.chef.adapter.in.web.PhoneVerifyRequest;
import com.mayo.server.chef.adapter.out.persistence.FindIdResponse;
import com.mayo.server.chef.app.port.in.ChefCommandInputPort;
import com.mayo.server.chef.app.port.in.SimpleEmailServiceInputPort;
import com.mayo.server.chef.app.port.in.SimpleMailServiceInputPort;
import com.mayo.server.chef.app.port.in.VerifyUseCase;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.enums.VerifyCode;
import com.mayo.server.common.exception.*;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.common.utility.GenerateUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.mayo.server.common.Constants.yyyy_MM_DD_HH_mm_ss;

@Service
@RequiredArgsConstructor
public class VerifyService implements VerifyUseCase {

    private final ChefQueryOutputPort outputPort;
    private final ChefCommandInputPort inputPort;
    private final SimpleMailServiceInputPort sendSnsInputPort;
    private final SimpleEmailServiceInputPort sendSesInputPort;

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public void send(PhoneRequest.RegisterRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        ChefPhone phone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.REGISTER.name());
        if(Objects.nonNull(phone)) {
            if (phone.getIsVerified() == 1) {
                throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_PHONE);
            }

            inputPort.postPhone(
                    ChefPhone
                            .builder()
                            .id(phone.getId())
                            .phone(req.phone())
                            .type(phone.getType())
                            .createdAt(DateUtility.getUTC0String(yyyy_MM_DD_HH_mm_ss))
                            .authNum(authNum)
                            .isVerified(0)
                            .build()
            );
        } else {

            inputPort.postPhone(req, authNum, VerifyCode.REGISTER.name());
        }

        sendSnsInputPort.send(req.phone(), authNum, VerifyCode.REGISTER);

    }

    @Override
    @Transactional
    public void send(PhoneRequest.UsernameRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef isChef = outputPort.getChefByPhone(req.phone());
        if(Objects.isNull(isChef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        Chef chef = outputPort.getChefByPhone(req.phone());
        if(Objects.isNull(chef)){
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.USERNAME.name());
        boolean isUsernameRow = Objects.nonNull(chefPhone);
        if(isUsernameRow) {
            inputPort.updateResendByFindUsername(chefPhone.getId(), authNum);
        } else {
            inputPort.postPhone(req, authNum, VerifyCode.USERNAME.name());
        }

        sendSnsInputPort.send(req.phone(), authNum, VerifyCode.USERNAME);

    }

    @Override
    @Transactional
    public void send(PhoneRequest.PwdRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef chef = outputPort.getChefByPhone(req.phone());
        if(Objects.isNull(chef)){
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.PWD.name());
        boolean isUsernameRow = Objects.nonNull(chefPhone);
        if(isUsernameRow) {
            inputPort.updateResendByFindPwd(chefPhone.getId(), authNum);
        } else {
            inputPort.postPhone(req, authNum, VerifyCode.PWD.name());
        }

        sendSnsInputPort.send(req.phone(), authNum, VerifyCode.PWD);

    }

    @Override
    public void send(PhoneRequest.MyPageRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef chef = outputPort.getChefByPhone(req.phone());
        if(Objects.isNull(chef)){
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.MY_PAGE.name());
        boolean isUsernameRow = Objects.nonNull(chefPhone);
        if(isUsernameRow) {
            inputPort.updateResendByFindPwd(chefPhone.getId(), authNum);
        } else {
            inputPort.postPhone(req, authNum, VerifyCode.MY_PAGE.name());
        }

        sendSnsInputPort.send(req.phone(), authNum, VerifyCode.MY_PAGE);

    }

    @Override
    @Transactional
    public void send(EmailRequest.RegisterRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        ChefEmail email = outputPort.getEmailVerifyByType(req.email(), VerifyCode.REGISTER.name());
        if(Objects.nonNull(email)) {
            if (email.getIsVerified() == 1) {
                throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_EMAIL);
            }

            inputPort.updateResendEmailByRegister(req.email(), authNum);

        } else {

            inputPort.postEmail(req, authNum, VerifyCode.REGISTER.name());
        }

        sendSesInputPort.send(req.email(), authNum, VerifyCode.REGISTER);

    }

    @Override
    @Transactional
    public void send(EmailRequest.UsernameRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef chef = outputPort.getChefByEmail(req.email());
        if(Objects.isNull(chef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefEmail email = outputPort.getEmailVerifyByType(req.email(), VerifyCode.USERNAME.name());
        if(Objects.nonNull(email)) {

            inputPort.updateResendEmailByFindUsername(req.email(), authNum);

        } else {

            inputPort.postEmail(req, authNum, VerifyCode.USERNAME.name());
        }

        sendSesInputPort.send(req.email(), authNum, VerifyCode.USERNAME);

    }

    @Override
    @Transactional
    public void send(EmailRequest.PwdRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef chef = outputPort.getChefByEmail(req.email());
        if(Objects.isNull(chef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefEmail email = outputPort.getEmailVerifyByType(req.email(), VerifyCode.PWD.name());
        if(Objects.nonNull(email)) {

            inputPort.updateResendEmailByPwd(req.email(), authNum);

        } else {

            inputPort.postEmail(req, authNum, VerifyCode.PWD.name());
        }

        sendSesInputPort.send(req.email(), authNum, VerifyCode.PWD);

    }

    @Override
    public void send(EmailRequest.MyPageRequest req) {

        String authNum = GenerateUtility.generateRandomCode();

        Chef chef = outputPort.getChefByEmail(req.email());
        if(Objects.isNull(chef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        ChefEmail email = outputPort.getEmailVerifyByType(req.email(), VerifyCode.MY_PAGE.name());
        if(Objects.nonNull(email)) {

            inputPort.updateResendEmailByPwd(req.email(), authNum);

        } else {

            inputPort.postEmail(req, authNum, VerifyCode.MY_PAGE.name());
        }

        sendSesInputPort.send(req.email(), authNum, VerifyCode.MY_PAGE);

    }

    @Override
    @Transactional
    public FindIdResponse verify(PhoneVerifyRequest.UsernameRequest req) {


        Long isRegisterConditionName = outputPort.existsByPhoneRegister(
                req.phone(),
                req.name(),
                VerifyCode.REGISTER.name(),
                1
        );

        if(Objects.isNull(isRegisterConditionName)){

            ErrorCode errorCode = ErrorCode.INVALID_PHONE_NAME_NOT_EQUALS;

            Boolean isRegister = outputPort.existsByPhoneRegister(
                    req.phone(),
                    VerifyCode.REGISTER.name(),
                    1
            );
            if(isRegister) {
                throw new InvalidRequestException(errorCode, errorCode.getMessage());
            };

            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);

        }

        ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.USERNAME.name());

        if(Objects.nonNull(chefPhone) && chefPhone.getIsVerified().equals(1)) {
            throw new NotGenerateException(ErrorCode.CHEF_PHONE_USERNAME_NOT_FOUND);
        }

        if(-5 > (DateUtility.getNowDiffTime(chefPhone.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
            throw new InvalidRequestException(ErrorCode.VERIFY_EXPIRED, ErrorCode.VERIFY_EXPIRED.getMessage());
        }

        if(!chefPhone.getAuthNum().equals(req.authNum())) {
            throw new InvalidRequestException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS, ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage());
        }

        Chef chef = outputPort.getChefByPhone(req.phone());

        return new FindIdResponse(
                chef.getChefUsername(),
                chef.getCreatedAt(),
                "chef"
        );

    }

    @Override
    @Transactional
    public JwtToken verify(PhoneVerifyRequest.PwdRequest req) {

        Chef chef = outputPort.getChefByUsername(req.username());
        if(Objects.isNull(chef)){
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        if(!chef.getChefName().equals(req.name())) {
            throw new NotEqualException(ErrorCode.INVALID_PHONE_NAME_NOT_EQUALS);
        }

        if(!chef.getChefBirthday().equals(req.birthday())) {
            throw new NotEqualException(ErrorCode.INVALID_BIRTHDAY_NOT_EQUALS);
        }

        ChefPhone chefPhone = outputPort.getPhoneVerifyByType(req.phone(), VerifyCode.PWD.name());
        if(Objects.nonNull(chefPhone) && chefPhone.getIsVerified().equals(1)) {
            throw new NotGenerateException(ErrorCode.CHEF_PHONE_USERNAME_NOT_FOUND);
        }

        if(-5 > (DateUtility.getNowDiffTime(chefPhone.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
            throw new InvalidRequestException(
                    ErrorCode.VERIFY_EXPIRED,
                    ErrorCode.VERIFY_EXPIRED.getMessage()
            );
        }

        if(!chefPhone.getAuthNum().equals(req.authNum())) {
            throw new InvalidRequestException(
                    ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS,
                    ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage()
            );
        }

        return jwtTokenProvider.createChefJwtToken(chef.getId());

    }

    @Override
    @Transactional
    public FindIdResponse verify(EmailVerifyRequest.UsernameRequest req) {

        Long isChef = outputPort.existsByEmailRegister(
                req.email(),
                req.name(),
                VerifyCode.REGISTER.name(),
                1
        );

        if(Objects.isNull(isChef)){

            ErrorCode errorCode = ErrorCode.INVALID_PHONE_NAME_NOT_EQUALS;

            Boolean isRegister = outputPort.existsByEmailRegister(
                    req.email(),
                    VerifyCode.REGISTER.name(),
                    1
            );

            if(isRegister) {
                throw new InvalidRequestException(errorCode, errorCode.getMessage());
            };

            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);

        }

        ChefEmail chefPhone = outputPort.getEmailVerifyByType(req.email(), VerifyCode.USERNAME.name());

        if(Objects.nonNull(chefPhone) && chefPhone.getIsVerified().equals(1)) {
            throw new NotGenerateException(ErrorCode.CHEF_PHONE_USERNAME_NOT_FOUND);
        }

        if(-5 > (DateUtility.getNowDiffTime(chefPhone.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
            throw new InvalidRequestException(ErrorCode.VERIFY_EXPIRED, ErrorCode.VERIFY_EXPIRED.getMessage());
        }

        if(!chefPhone.getAuthNum().equals(req.authNum())) {
            throw new InvalidRequestException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS, ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage());
        }

        Chef chef = outputPort.getChefByEmail(req.email());

        return new FindIdResponse(
                chef.getChefUsername(),
                chef.getCreatedAt(),
                "chef"
        );
    }

    @Override
    @Transactional
    public JwtToken verify(EmailVerifyRequest.PwdRequest req) {

        Chef chef = outputPort.getChefByUsername(req.username());
        if(Objects.isNull(chef)){
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        if(!chef.getChefName().equals(req.name())) {
            throw new NotEqualException(ErrorCode.INVALID_PHONE_NAME_NOT_EQUALS);
        }

        if(!chef.getChefBirthday().equals(req.birthday())) {
            throw new NotEqualException(ErrorCode.INVALID_BIRTHDAY_NOT_EQUALS);
        }

        ChefEmail chefEmail = outputPort.getEmailVerifyByType(req.email(), VerifyCode.PWD.name());
        if(Objects.nonNull(chefEmail) && chefEmail.getIsVerified().equals(1)) {
            throw new NotGenerateException(ErrorCode.CHEF_PHONE_USERNAME_NOT_FOUND);
        }

        if(-5 > (DateUtility.getNowDiffTime(chefEmail.getCreatedAt(), yyyy_MM_DD_HH_mm_ss) / 60)) {
            throw new InvalidRequestException(
                    ErrorCode.VERIFY_EXPIRED,
                    ErrorCode.VERIFY_EXPIRED.getMessage()
            );
        }

        if(!chefEmail.getAuthNum().equals(req.authNum())) {
            throw new InvalidRequestException(
                    ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS,
                    ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS.getMessage()
            );
        }

        return jwtTokenProvider.createChefJwtToken(chef.getId());

    }
}
