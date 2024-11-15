package com.mayo.server.party.app.service;

import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.app.port.out.PartyBoardUseCase;
import com.mayo.server.party.app.port.out.PartyOutputPort;
import com.mayo.server.common.PaginationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyBoardService implements PartyBoardUseCase {

    private final PartyOutputPort partyOutputPort;

    @Override
    public void calendar() {

    }

    @Override
    public PartyBoardResponse.HomePartyPage homeParty(PaginationDto dto, HomePartyRequest req) {

        return partyOutputPort.getCustomerHomeParties(dto.getPageRequest(), req);

    }

    @Override
    public PartyBoardResponse.HomePartyPage homeParty(PaginationDto dto, HomePartyListRequest req, Long id) {
        return partyOutputPort.getCustomerHomeParties(dto.getPageRequest(), req, id);
    }

    @Override
    public PartyBoardResponse.HomePartyDetails homePartyDetails(Long id) {

        return PartyBoardResponse.HomePartyDetails.getHomeParty(partyOutputPort.getCustomerHomeParty(id));
    }
}
