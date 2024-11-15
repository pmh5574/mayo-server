package com.mayo.server.account.adapter.in.web;

public class AccountRequest {

    public record BankRequest(
            String bankCode
    ) {

    }

    public record AccountRegisterRequest(

            String account
    ) {}
}
