package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerEdit(

        @NotNull(message = "이름을 입력해 주세요.")
        @NotEmpty(message = "이름을 입력해 주세요.")
        String name,

        @NotNull(message = "생년월일을 입력해 주세요.")
        @NotEmpty(message = "생년월일을 입력해 주세요.")
        @Pattern(
                regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                message = "생년월일은 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
        )
        String birthday,

        String email,

        String emailAuthNum,

        String phone,

        String phoneAuthNum
) {
}
