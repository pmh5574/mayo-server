package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VerifyCustomerIdByEmailCheck(
        @NotNull(message = "이름을 입력해 주세요.")
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotNull(message = "이메일 주소를 입력해 주세요.")
        @NotBlank(message = "이메일 주소를 입력해 주세요.")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "유효한 이메일 주소를 입력해 주세요."
        )
        String email,

        @NotNull(message = "인증번호 입력해 주세요.")
        @NotBlank(message = "인증번호를 입력해 주세요.")
        String authCode
) {
}
