package com.mayo.server.account.app.port.out;

import com.mayo.server.account.domain.models.ChefAccount;

public interface AccountQueryUseCase {

    ChefAccount getAccount(Long id);
}
