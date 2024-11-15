package com.mayo.server.account.domain.component;

import com.mayo.server.account.app.port.in.AccountInputPort;
import com.mayo.server.account.domain.models.ChefAccount;
import com.mayo.server.account.domain.repository.ChefAccountRepository;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.utility.DateUtility;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class AccountInputAdapter implements AccountInputPort {

    private final ChefAccountRepository chefAccountRepository;

    @Override
    public void saveBank(Long id, String bank) {

        ChefAccount chefAccount = chefAccountRepository.findByChefId(id);
        if(chefAccount == null) {
            chefAccountRepository.save(
                    ChefAccount
                            .builder()
                            .chefId(id)
                            .bank(bank)
                            .build()
            );
        } else {
            chefAccount.setBank(bank);
            chefAccountRepository.save(chefAccount);
        }

    }

    @Override
    public void saveAccount(Long id, String account) {

        ChefAccount chefAccount = chefAccountRepository.findByChefId(id);
        if(chefAccount == null) {
            throw new InvalidRequestException(
                    ErrorCode.CHEF_BANK_NOT_REGISTER,
                    ErrorCode.CHEF_BANK_NOT_REGISTER.getMessage()
            );
        }

        chefAccount.setAccount(account);
        chefAccount.setCreatedAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss));

        chefAccountRepository.save(chefAccount);

    }
}
