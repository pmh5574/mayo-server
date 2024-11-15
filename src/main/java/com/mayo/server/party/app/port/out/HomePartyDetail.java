package com.mayo.server.party.app.port.out;

import com.mayo.server.party.domain.enums.HomePartyStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record HomePartyDetail(
        Long customerHomePartyId,
        HomePartyStatus partyStatus,
        String partyInfo,
        BigDecimal budget,
        LocalDateTime partySchedule,
        Integer adultCount,
        Integer childCount,
        String partyComment,
        Set<String> partyServices,
        HomePartyChefDetail chef,
        HomePartyKitchenDetail kitchen

) {
    public String getCustomerHomePartyId() {
        return customerHomePartyId.toString();
    }

    public String getPartyStatus() {
        return partyStatus.toString();
    }
}
