package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerLoginRequest(
        @NotNull(message = "아이디를 입력해 주세요.")
        @NotBlank(message = "아이디를 입력해 주세요.")
        String username,

        @NotNull(message = "비밀번호를 입력해 주세요.")
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password) {
}
