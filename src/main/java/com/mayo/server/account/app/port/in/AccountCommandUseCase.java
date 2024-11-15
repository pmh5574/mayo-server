package com.mayo.server.account.app.port.in;

import com.mayo.server.account.adapter.in.web.AccountRequest;

public interface AccountCommandUseCase {

    void bank(Long id, AccountRequest.BankRequest req);

    void account(Long id, AccountRequest.AccountRegisterRequest req);

    void birthDay();
}
