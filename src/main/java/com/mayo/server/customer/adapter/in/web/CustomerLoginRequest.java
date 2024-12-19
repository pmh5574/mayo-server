package com.mayo.server.customer.adapter.in.web;

import com.mayo.server.customer.app.port.in.request.CustomerLoginServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record CustomerLoginRequest(
        @NotBlank(message = "아이디를 입력해 주세요.")
        String username,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password
) {

        public CustomerLoginServiceRequest toServiceRequest() {
                return new CustomerLoginServiceRequest(username, password);
        }
}
