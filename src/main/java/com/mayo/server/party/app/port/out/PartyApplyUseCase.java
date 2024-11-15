package com.mayo.server.party.app.port.out;

import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.MyMatchedHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.MyWaitHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyApplyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;

import java.util.List;

public interface PartyApplyUseCase {

    List<PartyApplyResponse.ApplyList> getApplyList(Long id);

    List<PartyApplyResponse.MatchingWaitingList> getMatchingWaitingList(Long id, PaginationWithTimeDto dto);

    List<PartyApplyResponse.MatchingList> getMatchedList(Long id, PaginationWithTimeDto dto);

    List<PartyApplyResponse.VisitList> getVisitList(Long id, PaginationWithTimeDto dto);

    HomePartyDetailDto getCustomerHomeParty(Long id);

    List<MyWaitHomePartyResponse> getMyWaitHomePartyList(Long id, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    List<MyMatchedHomePartyResponse> getMyMatchedHomePartyList(Long id, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    List<MyMatchedHomePartyResponse> getMyFinishedHomePartyList(Long id, PaginationWithTimeDto dto);

    Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto);

    PartyReviewDto getHomePartyReview(Long customerHomePartyId);

}
