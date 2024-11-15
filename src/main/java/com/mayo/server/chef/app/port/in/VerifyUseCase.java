package com.mayo.server.chef.app.port.in;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.chef.adapter.in.web.EmailRequest;
import com.mayo.server.chef.adapter.in.web.EmailVerifyRequest;
import com.mayo.server.chef.adapter.in.web.PhoneRequest;
import com.mayo.server.chef.adapter.in.web.PhoneVerifyRequest;
import com.mayo.server.chef.adapter.out.persistence.FindIdResponse;
import com.mayo.server.chef.adapter.out.persistence.VerifyResponse;
import software.amazon.awssdk.services.sns.endpoints.internal.Value;

public interface VerifyUseCase {

    void send(PhoneRequest.RegisterRequest req);

    void send(PhoneRequest.UsernameRequest req);

    void send(PhoneRequest.PwdRequest req);

    void send(PhoneRequest.MyPageRequest req);

    void send(EmailRequest.RegisterRequest req);

    void send(EmailRequest.UsernameRequest req);

    void send(EmailRequest.PwdRequest req);

    void send(EmailRequest.MyPageRequest req);

    FindIdResponse verify(PhoneVerifyRequest.UsernameRequest req);

    JwtToken verify(PhoneVerifyRequest.PwdRequest req);

    FindIdResponse verify(EmailVerifyRequest.UsernameRequest req);

    JwtToken verify(EmailVerifyRequest.PwdRequest req);
}
