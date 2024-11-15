package com.mayo.server.party.adapter.out.persistence;

import com.mayo.server.customer.app.port.out.SingleKitchenImageDto;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.domain.model.CustomerHomeParty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

import java.util.List;

public class PartyBoardResponse {

    public record HomePartyPage(

            List<HomeParty> partyList,

            Long count
    ) {

    }

    public record HomeParty(

            Long id,

            String info,

            LocalDateTime schedule,

            Integer numberOfPeople,

            BigDecimal budget,

            List<String> serviceList

    ) {

        public static List<HomeParty> getHomeParties(
                List<CustomerHomeParty> homePartyList
        ) {

            return homePartyList.stream().map(
                    homeParty -> new HomeParty(
                            homeParty.getCustomerHomePartyId(),
                            homeParty.getPartyComment(),
                            homeParty.getPartySchedule(),
                            homeParty.getNumberOfPeople(),
                            homeParty.getBudget(),
                            homeParty.getServiceList()
                    )
            ).toList();
        }

    }

    public record HomePartyDetails(

            Long id,

            String info,

            String address,

            String comment,

            BigDecimal budget,

            LocalDateTime scheduleAt,

            Integer numberOfPeople,

            Integer adult,

            Integer child,

            List<String> serviceList,

            String burnerType,

            List<SingleKitchenImageDto> kitchenImages,

            List<String> kitchenTools,

            String kitchenRequirements,

            String kitchenConsideration
    ) {

        public static HomePartyDetails getHomeParty(
                HomePartyDetailDto homePartyDetailDto
        ) {

            return new HomePartyDetails(
                    homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                    homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                    homePartyDetailDto.getAddress(),
                    homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                    homePartyDetailDto.getCustomerHomeParty().getBudget(),
                    homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                    homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                    homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                    homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                    homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                    homePartyDetailDto.getBurnerType(),
                    homePartyDetailDto.getKitchenImages(),
                    homePartyDetailDto.getKitchenTools(),
                    homePartyDetailDto.getKitchenRequirements(),
                    homePartyDetailDto.getKitchenConsiderations()
            );
        }

    }
}
