package com.mayo.server.customer.app.port.in.request;

public record CustomerPasswordChangeServiceRequest(
        String userId,
        String password,
        String passwordCheck
) {
}
