package com.mayo.server.customer.app.port.in.request;

public record CustomerPhoneRegisterServiceRequest(

        String userId,
        String password,
        String name,
        String birthday,
        String phone,
        String authCode
) {
}
