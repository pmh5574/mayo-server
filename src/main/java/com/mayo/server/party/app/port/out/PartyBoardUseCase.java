package com.mayo.server.party.app.port.out;

import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.common.PaginationDto;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyBoardUseCase {

    void calendar();

    PartyBoardResponse.HomePartyPage homeParty(PaginationDto dto, HomePartyRequest req);

    PartyBoardResponse.HomePartyPage homeParty(PaginationDto dto, HomePartyListRequest req, Long id);

    PartyBoardResponse.HomePartyDetails homePartyDetails(Long id);

}
