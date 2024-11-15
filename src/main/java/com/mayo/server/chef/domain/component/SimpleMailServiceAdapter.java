package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.app.port.in.SimpleMailServiceInputPort;
import com.mayo.server.chef.infra.SimpleMailService;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.VerifyCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Adapter
@RequiredArgsConstructor
public class SimpleMailServiceAdapter implements SimpleMailServiceInputPort {

    private final SimpleMailService smsService;
    private final Map<VerifyCode, String> messageMap = Map.of(
            VerifyCode.REGISTER, "인증번호는 %s 입니다.",
            VerifyCode.USERNAME, "인증번호는 %s 입니다.",
            VerifyCode.PWD, "인증번호는 %s 입니다.",
            VerifyCode.MY_PAGE, "인증번호는 %s 입니다."
    );

    @Override
    public void send(String phone, String authNum, VerifyCode type) {

        smsService.send(getRegister(authNum, type), "+82" + phone);
    }

    private String getRegister(
            String authNum,
            VerifyCode type
    ) {

        String str = messageMap.get(type);

        return String.format(str, authNum);
    }

}
