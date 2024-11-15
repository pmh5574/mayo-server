package com.mayo.server.party.domain.repository;

import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CustomerHomPartyRepositoryCustom {

    PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyRequest req);

    PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyListRequest req, Long id);

    List<Long> getCustomerHomePartyIds(Pageable pageable, HomePartyRequest req);

    Optional<HomePartyDetail> getPartyDetail(Long partyId, List<HomePartyStatus> statuses, Long userId);

    List<Long> getMyWaitHomePartyIds(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    List<Long> getMyMatchedHomePartyIds(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    List<Long> getMyFinishedHomePartyIds(Long chefId, PaginationWithTimeDto dto);

    Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req);

    Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto);

    List<HomePartyFinishListDto> getFinishPartyList(CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest, Long userId, HomePartyStatus status,
                                                    Pageable pageable);

    List<CustomerHomeParty> getStatusListByCustomerId(Long userId);
}
