package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerPhoneVerifyRegisterRequest(

        @NotNull(message = "전화번호를 입력해 주세요.")
        @NotBlank(message = "전화번호를 입력해 주세요.")
        @Pattern(
                regexp = "^(?!.*-)[0-9]{10,11}$",
                message = "휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."
        )
        String phone
) {
}
