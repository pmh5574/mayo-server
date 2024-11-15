package com.mayo.server.party.domain.component;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.party.app.port.in.AcceptInputPort;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.repository.CustomerHomePartyRepository;
import com.mayo.server.party.domain.repository.PartyJpaScheduleRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class AcceptCommandAdapter implements AcceptInputPort {

    private final PartyJpaScheduleRepository partyJpaScheduleRepository;
    private final CustomerHomePartyRepository customerHomePartyRepository;

    @Override
    public void accepted(Long customerHomePartyId, Integer matched) {

        partyJpaScheduleRepository.updateIsMatchedByCustomerHomeParty_CustomerHomePartyId(customerHomePartyId, matched);

        customerHomePartyRepository.updatePartyStatusByCustomerHomePartyId(customerHomePartyId, HomePartyStatus.ACCEPTED);


    }

    @Override
    public void rejected(Long customerHomePartyId, Integer matched) {

        partyJpaScheduleRepository.updateIsMatchedByCustomerHomeParty_CustomerHomePartyId(customerHomePartyId, matched);

        customerHomePartyRepository.updatePartyStatusByCustomerHomePartyId(customerHomePartyId, HomePartyStatus.REJECTED);

    }
}
