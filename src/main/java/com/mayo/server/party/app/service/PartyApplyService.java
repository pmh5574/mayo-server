package com.mayo.server.party.app.service;

import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.MyMatchedHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.MyWaitHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyApplyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.app.port.out.PartyApplyUseCase;
import com.mayo.server.party.app.port.out.PartyOutputPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyApplyService implements PartyApplyUseCase {

    private final PartyOutputPort partyOutputPort;

    @Override
    public List<PartyApplyResponse.ApplyList> getApplyList(Long id) {
        return partyOutputPort.getApplyList(id);
    }

    @Override
    public List<PartyApplyResponse.MatchingWaitingList> getMatchingWaitingList(Long id, PaginationWithTimeDto dto) {
        return partyOutputPort.getMatchingWaitingList(id, dto);
    }


    @Override
    public List<PartyApplyResponse.MatchingList> getMatchedList(Long id, PaginationWithTimeDto dto) {
        return partyOutputPort.getMatchedList(id, dto);
    }

    @Override
    public List<PartyApplyResponse.VisitList> getVisitList(Long id, PaginationWithTimeDto dto) {
        return partyOutputPort.getVisitList(id, dto);
    }

    @Override
    public HomePartyDetailDto getCustomerHomeParty(Long id) {
        return partyOutputPort.getCustomerHomeParty(id);
    }

    @Override
    public List<MyWaitHomePartyResponse> getMyWaitHomePartyList(
            Long id,
            PaginationWithTimeDto dto,
            MyHomePartyListRequest req
    ) {

        return partyOutputPort.getMyWaitHomePartyList(id, dto, req).stream().map(party -> new MyWaitHomePartyResponse(
                party.info(),
                party.address(),
                DateUtility.replacePlainTimeFromLocaleDate(party.scheduleAt()),
                "어른: " + party.adultCount() + "명 어린이: " + party.childCount() + "명",
                party.budget(),
                DateUtility.replacePlainTimeFromLocaleDate(party.createdAt())
        )).toList();
    }

    @Override
    public List<MyMatchedHomePartyResponse> getMyMatchedHomePartyList(Long id, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return partyOutputPort.getMyMatchedHomePartyList(id, dto, req).stream().map(party -> new MyMatchedHomePartyResponse(
                party.info(),
                DateUtility.replacePlainTimeFromLocaleDate(party.scheduleAt())
        )).toList();
    }

    @Override
    public List<MyMatchedHomePartyResponse> getMyFinishedHomePartyList(Long id, PaginationWithTimeDto dto) {
        return partyOutputPort.getMyFinishedHomePartyList(id, dto).stream().map(party -> new MyMatchedHomePartyResponse(
                party.info(),
                DateUtility.replacePlainTimeFromLocaleDate(party.scheduleAt())
        )).toList();
    }

    @Override
    public Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return partyOutputPort.getMyWaitCount(chefId, dto, req);
    }

    @Override
    public Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return partyOutputPort.getMyMatchedCount(chefId, dto, req);
    }

    @Override
    public Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto) {
        return partyOutputPort.getMyFinishedCount(chefId, dto);
    }

    @Override
    public PartyReviewDto getHomePartyReview(Long customerHomePartyId) {
        return partyOutputPort.getHomePartyReview(customerHomePartyId);
    }

}
