package com.mayo.server.party.app.port.in;

import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartySchedule;

public interface PartyBoardInputPort {

    void postPartySchedule(Long id, Long partyId, HomePartyDetailDto dto);
}
