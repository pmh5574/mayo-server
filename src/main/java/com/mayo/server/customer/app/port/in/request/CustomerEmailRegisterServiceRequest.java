package com.mayo.server.customer.app.port.in.request;

public record CustomerEmailRegisterServiceRequest(
        String userId,
        String password,
        String name,
        String birthday,
        String email,
        String authCode
) {
}
