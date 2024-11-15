package com.mayo.server.chef.app.service;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.auth.domain.component.RefreshTokenQueryInput;
import com.mayo.server.chef.adapter.in.web.LoginRequest;
import com.mayo.server.chef.adapter.in.web.RegisterEmailRequest;
import com.mayo.server.chef.adapter.in.web.RegisterPhoneRequest;
import com.mayo.server.chef.adapter.in.web.UpdatePwdRequest;
import com.mayo.server.chef.app.port.in.ChefCommandInputPort;
import com.mayo.server.chef.app.port.in.ChefAuthUseCase;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.chef.infra.SimpleEmailService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.PwdUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChefChefAuthService implements ChefAuthUseCase {

    private final ChefQueryOutputPort outputPort;
    private final ChefCommandInputPort inputPort;
    private final RefreshTokenQueryInput refreshTokenQueryInput;

    private final JwtService jwt;
    private final SimpleEmailService simpleEmailService;

    @Override
    @Transactional
    public void registerByPhone(RegisterPhoneRequest req) {

        Boolean isUsername = outputPort.existsByChefUsername(req.username());
        if (isUsername) {
            throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_USERNAME);
        }

        ChefPhone phone = outputPort.getPhoneVerify(req.phone());
        if (Objects.isNull(phone)) {
            throw new NotFoundException(ErrorCode.CHEF_PHONE_NOT_FOUND);
        }
        if (phone.getIsVerified() == 1) {
            throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_PHONE);
        }
        if (!req.authNum().equals(phone.getAuthNum())) {
            throw new NotEqualException(ErrorCode.PHONE_AUTH_NUMBER_NOT_EQUALS);
        }

        String hashedPwd = PwdUtility.hash(req.password());

        inputPort.postChef(req, hashedPwd);

        inputPort.updatePhoneVerifyByPhone(req.phone());

    }

    @Override
    @Transactional
    public void registerByEmail(RegisterEmailRequest req) {

        Boolean isUsername = outputPort.existsByChefUsername(req.username());
        if (isUsername) {
            throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_USERNAME);
        }

        ChefEmail email = outputPort.getEmailVerify(req.email());
        if (Objects.isNull(email)) {
            throw new NotFoundException(ErrorCode.CHEF_EMAIL_NOT_FOUND);
        }
        if (email.getIsVerified() == 1) {
            throw new DuplicateException(ErrorCode.CHEF_DUPLICATED_EMAIL);
        }
        if (!req.authNum().equals(email.getAuthNum())) {
            throw new NotEqualException(ErrorCode.EMAIL_AUTH_NUMBER_NOT_EQUALS);
        }

        String hashedPwd = PwdUtility.hash(req.password());

        inputPort.postChef(req, hashedPwd);

        inputPort.updateEmailVerifyByPhone(req.email());

        simpleEmailService.send("[신규]요리사 회원가입 : " + req.email(), "<h1>가입 승인 필요</h1>", SimpleEmailService.FROM);

    }

    @Override
    @Transactional
    public JwtToken login(LoginRequest req) {

        Chef chef = outputPort.getChefByUsername(req.username());
        if (Objects.isNull(chef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }
        PwdUtility.comparedPwd(
                chef.getChefPassword(),
                req.password()
        );

        return jwt.createChefJwtToken(chef.getId());

    }

    @Override
    @Transactional
    public void updateChefPwd(UpdatePwdRequest req) {

        inputPort.updateChefPwd(req);

    }
}
