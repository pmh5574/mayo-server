package com.mayo.server.party.app.service;

import com.mayo.server.common.Constants;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.party.app.port.in.PartyBoardInputPort;
import com.mayo.server.party.app.port.in.PartyUseCase;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.app.port.out.PartyOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyService implements PartyUseCase {

    private final PartyOutputPort partyOutputPort;
    private final PartyBoardInputPort partyBoardInputPort;

    @Override
    @Transactional
    public void apply(Long id, Long partyId) {

        HomePartyDetailDto customerHomeParty = partyOutputPort.getCustomerHomeParty(partyId);
        if(DateUtility.getNowDiffTime(
                DateUtility.replaceDateForT(customerHomeParty.getCustomerHomeParty().getPartySchedule().toString()),
                Constants.yyyy_MM_DD_HH_mm) < 0
        ) {
            throw new InvalidRequestException(
                    ErrorCode.CHEF_PARTY_IS_EXPIRED,
                    ErrorCode.CHEF_PARTY_IS_EXPIRED.getMessage()
            );
        }

        boolean isMatched = partyOutputPort.existMatchedHomeParty(partyId);
        if(isMatched) {
            throw new InvalidRequestException(
                    ErrorCode.CHEF_PARTY_IS_MATCHED,
                    ErrorCode.CHEF_PARTY_IS_MATCHED.getMessage()
            );
        }

        partyBoardInputPort.postPartySchedule(
                id,
                partyId,
                customerHomeParty
        );

    }
}
