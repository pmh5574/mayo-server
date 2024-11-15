package com.mayo.server.party.app.port.out;

import java.time.LocalDateTime;
import java.util.Set;

public record PartyReviewListDto(
        Long id,

        Long homePartyId,

        String chefName,

        String reviewContent,

        LocalDateTime createdAt,

        Set<String> foodReason,

        Set<String> serviceReason
) {

    public String getId() {
        return String.valueOf(id);
    }

    public String getHomePartyId() {
        return String.valueOf(homePartyId);
    }
}
