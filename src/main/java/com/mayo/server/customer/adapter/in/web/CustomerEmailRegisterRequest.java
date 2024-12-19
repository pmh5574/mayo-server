package com.mayo.server.customer.adapter.in.web;

import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record CustomerEmailRegisterRequest(
        @NotBlank(message = "아이디를 입력해 주세요.")
        String userId,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "생년월일을 입력해 주세요.")
        String birthday,

        @NotBlank(message = "이메일 주소를 입력해 주세요.")
        String email,

        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {
        public CustomerEmailRegisterServiceRequest toServiceRequest() {
                return new CustomerEmailRegisterServiceRequest(userId, password, name, birthday, email, authCode);
        }
}
