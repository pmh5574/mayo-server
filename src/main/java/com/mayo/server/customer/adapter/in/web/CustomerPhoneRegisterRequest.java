package com.mayo.server.customer.adapter.in.web;

import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record CustomerPhoneRegisterRequest(
        @NotBlank(message = "아이디를 입력해 주세요.")
        String userId,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "생년월일을 입력해 주세요.")
        String birthday,

        @NotBlank(message = "전화번호를 입력해 주세요.")
        String phone,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {

        public CustomerPhoneRegisterServiceRequest toServiceRequest() {
                return new CustomerPhoneRegisterServiceRequest(
                        userId,
                        password,
                        name,
                        birthday,
                        phone,
                        authCode
                );
        }
}
