package com.mayo.server.account.adapter.out.persistence;

import com.mayo.server.account.domain.models.ChefAccount;

import java.util.Objects;

public record AccountResponse(

        String bank,

        String account
) {

    public static AccountResponse getAccount(ChefAccount chefAccount) {

        if(Objects.isNull(chefAccount)) {
            return new AccountResponse("", "");
        }
        return new AccountResponse(chefAccount.getBank(), Objects.isNull(chefAccount.getAccount()) ? "" : chefAccount.getAccount());
    }
}
