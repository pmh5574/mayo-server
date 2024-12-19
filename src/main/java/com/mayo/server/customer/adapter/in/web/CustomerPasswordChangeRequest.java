package com.mayo.server.customer.adapter.in.web;

import com.mayo.server.customer.app.port.in.request.CustomerPasswordChangeServiceRequest;
import jakarta.validation.constraints.NotBlank;

public record CustomerPasswordChangeRequest(
        @NotBlank(message = "잘못된 페이지 접근 입니다")
        String userId,

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
        String passwordCheck
) {
        public CustomerPasswordChangeServiceRequest toServiceRequest() {
                return new CustomerPasswordChangeServiceRequest(userId, password, passwordCheck);
        }
}
