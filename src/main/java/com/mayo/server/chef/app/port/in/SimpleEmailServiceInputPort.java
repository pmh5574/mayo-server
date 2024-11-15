package com.mayo.server.chef.app.port.in;

import com.mayo.server.common.enums.VerifyCode;

public interface SimpleEmailServiceInputPort {

    void send(String to, String authNum, VerifyCode code);

}
