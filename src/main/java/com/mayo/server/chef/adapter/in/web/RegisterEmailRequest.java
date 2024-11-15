package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterEmailRequest(

        @NotNull(message = "아이디를 입력해 주세요")
        @NotBlank(message = "아이디를 입력해 주세요")
        String username,
        @NotNull(message = "비밀번호를 입력해 주세요")
        @NotBlank(message = "비밀번호를 입력해 주세요")
        String password,

        @NotNull(message = "이름를 입력해 주세요")
        @NotBlank(message = "이름를 입력해 주세요")
        String name,

        @NotNull(message = "생년월일을 입력해 주세요")
        @NotBlank(message = "생년월일을 입력해 주세요")
        String birthday,

        @NotNull(message = "이메일을 입력해 주세요")
        @NotBlank(message = "이메일을 입력해 주세요")
        String email,

        @NotNull(message = "인증번호 입력해 주세요")
        @NotBlank(message = "인증번호를 입력해 주세요")
        String authNum
) {
}
