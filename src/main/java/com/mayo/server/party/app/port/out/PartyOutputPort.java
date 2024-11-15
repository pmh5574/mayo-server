package com.mayo.server.party.app.port.out;

import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.PartyApplyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartyOutputPort {

    PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyRequest req);

    PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyListRequest req, Long id);

    HomePartyDetailDto getCustomerHomeParty(Long id);

    boolean existMatchedHomeParty(Long id);

    List<PartyApplyResponse.ApplyList> getApplyList(Long id);

    List<PartyApplyResponse.MatchingWaitingList> getMatchingWaitingList(Long id,PaginationWithTimeDto dto);

    List<PartyApplyResponse.MatchingList> getMatchedList(Long id,PaginationWithTimeDto dto);

    List<PartyApplyResponse.VisitList> getVisitList(Long id,PaginationWithTimeDto dto);

    List<MyHomePartyDto> getMyWaitHomePartyList(Long id, PaginationWithTimeDto pageable, MyHomePartyListRequest req);

    List<MyHomePartyDto> getMyMatchedHomePartyList(Long id, PaginationWithTimeDto pageable, MyHomePartyListRequest req);

    List<MyHomePartyDto> getMyFinishedHomePartyList(Long id, PaginationWithTimeDto pageable);

    Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto);

    PartyReviewDto getHomePartyReview(Long customerHomePartyId);

}
