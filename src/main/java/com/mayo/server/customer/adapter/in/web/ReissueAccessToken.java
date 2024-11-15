package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReissueAccessToken(
        @NotNull(message = "refreshToken 값이 누락되었습니다.")
        @NotBlank(message = "refreshToken 값이 누락되었습니다.")
        String refreshToken
) {
}
