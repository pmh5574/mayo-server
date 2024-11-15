package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.app.port.in.SimpleEmailServiceInputPort;
import com.mayo.server.chef.infra.SimpleEmailService;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.VerifyCode;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class SimpleEmailServiceAdapter implements SimpleEmailServiceInputPort {

    private final SimpleEmailService sesClient;

    @Override
    public void send(String to, String authNum, VerifyCode code) {

        sesClient.send(getSubject(), getBody(authNum), to);

    }

    public String getBody(String authNum) {

        return String.format("인증 번호는 %s 입니다.", authNum);

    }

    public String getSubject() {

        return "Mayo 입니다. 이메일 인증번호를 확인해 주세요";
    }
}
