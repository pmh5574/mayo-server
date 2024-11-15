package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdatePwdRequest (

        @NotNull(message = "아이디를 입력해 주세요")
        @NotBlank(message = "아이디를 입력해 주세요")
        @Pattern(
                regexp = "^[a-zA-Z\\d]{4,20}$",
                message = "아이디는 4~20자리의 영문자와 숫자 조합이어야 합니다."
        )
        String username,
        @NotNull(message = "비밀번호를 입력해 주세요")
        @NotBlank(message = "비밀번호를 입력해 주세요")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{8,16}$",
                message = "비밀번호는 8~16자리의 영문 소문자와 숫자의 조합이어야 합니다."
        )
        String password
) {
}
