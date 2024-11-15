package com.mayo.server.chef.app.port.in;

import com.mayo.server.chef.adapter.in.web.*;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.model.ChefPhone;
import com.mayo.server.common.enums.VerifyCode;

public interface ChefCommandInputPort {

    Chef postChef(RegisterPhoneRequest req, String hashedPwd);

    Chef postChef(RegisterEmailRequest req, String hashedPwd);

    void updateChefPwd(UpdatePwdRequest req);

    void postPhone(PhoneRequest.RegisterRequest req, String authNum, String type);

    void postPhone(PhoneRequest.UsernameRequest req, String authNum, String type);

    void postPhone(PhoneRequest.MyPageRequest req, String authNum, String type);

    void updateResendByFindUsername(Long id, String authNum);

    void postPhone(PhoneRequest.PwdRequest req, String authNum, String type);

    void updateResendByFindPwd(Long id, String authNum);

    void postPhone(ChefPhone chefPhone);

    void postEmail(EmailRequest.RegisterRequest req, String authNum, String type);

    void updateResendEmailByRegister(String email, String authNum);

    void postEmail(EmailRequest.UsernameRequest req, String authNum, String type);

    void updateResendEmailByFindUsername(String email, String authNum);

    void postEmail(EmailRequest.PwdRequest req, String authNum, String type);

    void updateResendEmailByPwd(String email, String authNum);

    void postEmail(EmailRequest.MyPageRequest req, String authNum, String type);

    void updatePhoneVerifyByPhone(String phone);

    void updateEmailVerifyByPhone(String email);

    void updateMyPage(MyPageRequest.ChefInfoRequest req, Long id);

}
