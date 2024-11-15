package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerifyEmailAndCodeAndUserTypeForPasswordChange(
        @NotNull(message = "잘못된 페이지 접근 입니다.")
        @NotBlank(message = "잘못된 페이지 접근 입니다")
        String userId,

        @NotNull(message = "잘못된 페이지 접근 입니다")
        @NotBlank(message = "잘못된 페이지 접근 입니다")
        String email,

        @NotNull(message = "잘못된 페이지 접근 입니다")
        @NotBlank(message = "잘못된 페이지 접근 입니다")
        String authCode
) {
}
