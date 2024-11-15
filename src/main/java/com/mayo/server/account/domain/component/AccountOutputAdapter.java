package com.mayo.server.account.domain.component;

import com.mayo.server.account.app.port.out.AccountOutputPort;
import com.mayo.server.account.domain.models.ChefAccount;
import com.mayo.server.account.domain.repository.ChefAccountRepository;
import com.mayo.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class AccountOutputAdapter implements AccountOutputPort {

    private final ChefAccountRepository chefAccountRepository;

    @Override
    public ChefAccount getAccount(Long id) {
        return chefAccountRepository.findByChefId(id);
    }
}
