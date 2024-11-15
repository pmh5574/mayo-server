package com.mayo.server.party.app.port.in;

import com.mayo.server.party.adapter.in.web.PartyReviewRegisterRequest;
import com.mayo.server.party.app.port.out.PartyReviewListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import java.util.List;

public interface PartyReviewQueryInputPort {

    Long saveReview(PartyReviewRegisterRequest partyReviewRegisterRequest, CustomerHomeParty customerHomeParty);

    CustomerHomeParty findByCustomerHomeParty(Long homePartyId, Long userId, HomePartyStatus status);

    Boolean checkReviewExists(CustomerHomeParty customerHomeParty);

    List<PartyReviewListDto> getReviewList(Long userId);
}
