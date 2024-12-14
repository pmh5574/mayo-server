package com.mayo.server.party.app.port.in;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.party.adapter.in.web.CustomerPartyChefRegisterRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyRegisterRequest;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import com.mayo.server.party.app.port.out.HomePartyNoReviewFinishListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartySchedule;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomerPartyQueryInputPort {
    Long postHomePartyToChef(CustomerPartyChefRegisterRequest customerPartyChefRegisterRequest, Long userId, Chef chef,
                             Kitchen kitchen);

    Chef findByChef(Long chefId);

    Long postHomeParty(CustomerPartyRegisterRequest customerPartyRegisterRequest, Long userId, Kitchen kitchen);

    List<CustomerHomeParty> getStatusListByCustomerId(Long userId);

    HomePartyDetail getPartyDetail(String homePartyId, Long userId, List<HomePartyStatus> statuses);

    CustomerHomeParty findByCustomerHomePartyReview(Long homePartyId, Long userId, HomePartyStatus status);

    List<HomePartyFinishListDto> getFinishPartyList(CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest, Long userId, HomePartyStatus finish,
                                                    Pageable pageable);

    Kitchen findByKitchen(Long kitchenId, Long customerId);

    List<Kitchen> getKitchenList(Long userId);

    List<PartySchedule> getChefIdList(Long customerHomePartyId);

    List<ChefNotSelectedDto> getChefNotSelectedList(List<PartySchedule> partyScheduleList);

    PartySchedule getPartySchedule(Long partyScheduleId, Long userId);

    List<HomePartyNoReviewFinishListDto> getFinishPartyNoReviewList(Long userId, HomePartyStatus homePartyStatus);

    Long getFinishPartyListTotalCount(String startDate, String endDate, Long userId, HomePartyStatus finish);
}
