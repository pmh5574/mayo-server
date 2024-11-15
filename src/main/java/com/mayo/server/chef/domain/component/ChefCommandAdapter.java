package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.adapter.in.web.*;
import com.mayo.server.chef.app.port.in.ChefCommandInputPort;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefEmail;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.chef.domain.repository.ChefEmailJpaRepository;
import com.mayo.server.chef.domain.repository.ChefJpaRepository;
import com.mayo.server.chef.domain.repository.ChefPhoneJpaRepository;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.common.utility.PwdUtility;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Adapter
@RequiredArgsConstructor
public class ChefCommandAdapter implements ChefCommandInputPort {

    private final ChefJpaRepository chefJpaRepository;
    private final ChefPhoneJpaRepository phoneJpaRepository;
    private final ChefEmailJpaRepository emailJpaRepository;

    @Override
    public Chef postChef(RegisterPhoneRequest req, String hashedPwd) {
        return chefJpaRepository.save(Chef
                .builder()
                .chefUsername(req.username())
                .chefPassword(hashedPwd)
                .chefName(req.name())
                .chefBirthday(req.birthday())
                .chefPhone(req.phone())
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public Chef postChef(RegisterEmailRequest req, String hashedPwd) {
        return chefJpaRepository.save(Chef
                .builder()
                .chefUsername(req.username())
                .chefPassword(hashedPwd)
                .chefName(req.name())
                .chefBirthday(req.birthday())
                .chefEmail(req.email())
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateChefPwd(UpdatePwdRequest req) {
        chefJpaRepository.updateChefPwd(
                req.username(),
                PwdUtility.hash(req.password())
        );
    }

    @Override
    public void postPhone(PhoneRequest.RegisterRequest req, String authNum, String type) {
        phoneJpaRepository.save(ChefPhone
                .builder()
                .phone(req.phone())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .build()
        );
    }

    @Override
    public void postPhone(PhoneRequest.UsernameRequest req, String authNum, String type) {
        phoneJpaRepository.save(ChefPhone
                .builder()
                .phone(req.phone())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                        .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void postPhone(PhoneRequest.MyPageRequest req, String authNum, String type) {
        phoneJpaRepository.save(ChefPhone
                .builder()
                .phone(req.phone())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateResendByFindUsername(Long id, String authNum) {
        phoneJpaRepository.updateResendByFindUsername(
                id,
                authNum,
                DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)
        );
    }

    @Override
    public void postPhone(PhoneRequest.PwdRequest req, String authNum, String type) {
        phoneJpaRepository.save(ChefPhone
                .builder()
                .phone(req.phone())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateResendByFindPwd(Long id, String authNum) {
        phoneJpaRepository.updateResendByFindPwd(
                id,
                authNum,
                DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)
        );
    }

    @Override
    public void postPhone(ChefPhone chefPhone) {
        phoneJpaRepository.save(chefPhone);
    }

    @Override
    public void postEmail(EmailRequest.RegisterRequest req, String authNum, String type) {
        emailJpaRepository.save(ChefEmail
                .builder()
                .email(req.email())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateResendEmailByRegister(String email, String authNum) {
        emailJpaRepository.updateResendEmailByRegister(
                email,
                authNum,
                DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)
        );
    }

    @Override
    public void postEmail(EmailRequest.UsernameRequest req, String authNum, String type) {
        emailJpaRepository.save(ChefEmail
                .builder()
                .email(req.email())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateResendEmailByFindUsername(String email, String authNum) {
        emailJpaRepository.updateResendEmailByFindUsername(
                email,
                authNum,
                DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)
        );
    }

    @Override
    public void postEmail(EmailRequest.PwdRequest req, String authNum, String type) {
        emailJpaRepository.save(ChefEmail
                .builder()
                .email(req.email())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updateResendEmailByPwd(String email, String authNum) {
        emailJpaRepository.updateResendEmailByFindPwd(
                email,
                authNum,
                DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)
        );
    }

    @Override
    public void postEmail(EmailRequest.MyPageRequest req, String authNum, String type) {
        emailJpaRepository.save(ChefEmail
                .builder()
                .email(req.email())
                .authNum(authNum)
                .type(type)
                .isVerified(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build()
        );
    }

    @Override
    public void updatePhoneVerifyByPhone(String phone) {
        phoneJpaRepository.updatePhoneVerifyByPhone(phone);
    }

    @Override
    public void updateEmailVerifyByPhone(String email) {
        emailJpaRepository.updateChefEmailByEmail(email);
    }

    @Override
    public void updateMyPage(MyPageRequest.ChefInfoRequest req, Long id) {

        if(Objects.isNull(req.email())) {

            chefJpaRepository.updateChefInfoPhone(
                    req.name(),
                    req.birthday(),
                    req.phone(),
                    id
            );
        }

        if(Objects.isNull(req.phone())) {

            chefJpaRepository.updateChefInfoEmail(
                    req.name(),
                    req.birthday(),
                    req.email(),
                    id
            );

        }

        if(Objects.isNull(req.phone()) && Objects.isNull(req.email())) {

            chefJpaRepository.updateChefInfoNormal(
                    req.name(),
                    req.birthday(),
                    id
            );

        }

        chefJpaRepository.updateChefInfoAll(
                req.name(),
                req.birthday(),
                req.email(),
                req.phone(),
                id
        );
    }
}
