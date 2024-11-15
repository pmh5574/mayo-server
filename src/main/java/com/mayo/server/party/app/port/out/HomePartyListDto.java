package com.mayo.server.party.app.port.out;

import com.mayo.server.party.domain.model.CustomerHomeParty;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

import java.util.List;

public record HomePartyListDto(

        Long id,

        String introduce,

        LocalDateTime date,

        Integer numberOfPeople,

        List<String> serviceList

) {

    public static List<HomePartyListDto> getHomeParties(
            Page<CustomerHomeParty> homePartyList
    ) {

        return homePartyList.stream().map(
                homeParty ->  {
                    return new HomePartyListDto(
                            homeParty.getCustomerHomePartyId(),
                            homeParty.getPartyComment(),
                            homeParty.getPartySchedule(),
                            homeParty.getNumberOfPeople(),
                            homeParty.getServiceList()
                    );
                }
        ).toList();
    }

}
