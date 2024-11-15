package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerPasswordChange(
        @NotNull(message = "잘못된 페이지 접근 입니다.")
        @NotBlank(message = "잘못된 페이지 접근 입니다")
        String userId,

        @NotNull(message = "비밀번호를 입력해 주세요.")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$",
                message = "비밀번호는 8~16자리의 영문 소문자와 숫자의 조합이어야 합니다."
        )
        String password,

        @NotNull(message = "비밀번호 확인을 입력해 주세요.")
        @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$",
                message = "비밀번호는 8~16자리의 영문 소문자와 숫자의 조합이어야 합니다."
        )
        String passwordCheck
) {
}
