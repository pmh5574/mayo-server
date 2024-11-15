package com.mayo.server.chef.app.port.in;

import com.mayo.server.common.enums.VerifyCode;

public interface SimpleMailServiceInputPort {

    void send(String phone, String authNum, VerifyCode type);

}
