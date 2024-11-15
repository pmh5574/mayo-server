package com.mayo.server.account.adapter.in.web;

import com.mayo.server.account.domain.models.BankCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerAccountRequest(

        @NotNull(message = "은행을 입력해 주세요.")
        BankCode bank,

        @NotNull(message = "계좌번호를 입력해 주세요.")
        @NotBlank(message = "계좌번호를 입력해 주세요.")
        String account
) {
}
