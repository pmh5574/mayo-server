package com.mayo.server.customer.app.port.in.request;

public record CustomerLoginServiceRequest(
        String username,
        String password
) {

}
