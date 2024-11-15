package com.mayo.server.account.adapter.out.persistence;

import com.mayo.server.account.domain.models.BankCode;

public record CustomerAccountResponse(
        BankCode bank,
        String account
) {
}
