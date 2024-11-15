package com.mayo.server.account.app.service;

import com.mayo.server.account.app.port.out.AccountOutputPort;
import com.mayo.server.account.app.port.out.AccountQueryUseCase;
import com.mayo.server.account.domain.models.ChefAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService implements AccountQueryUseCase {

    private final AccountOutputPort accountOutputPort;

    @Override
    public ChefAccount getAccount(Long id) {
        return accountOutputPort.getAccount(id);
    }
}
