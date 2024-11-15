package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerEmailRegisterRequest(
        @NotNull(message = "아이디를 입력해 주세요.")
        @NotBlank(message = "아이디를 입력해 주세요.")
        @Pattern(
                regexp = "^[a-zA-Z\\d]{4,20}$",
                message = "아이디는 4~20자리의 영문자와 숫자 조합이어야 합니다."
        )
        String userId,

        @NotNull(message = "비밀번호를 입력해 주세요.")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$",
                message = "비밀번호는 8~16자리의 영문 소문자와 숫자의 조합이어야 합니다."
        )
        String password,

        @NotNull(message = "이름을 입력해 주세요.")
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotNull(message = "생년월일을 입력해 주세요.")
        @NotBlank(message = "생년월일을 입력해 주세요.")
        @Pattern(
                regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                message = "생년월일은 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
        )
        String birthday,

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
