package com.mayo.server.party.app.port.out;

import java.time.LocalDateTime;
import java.util.Set;

public record HomePartyNoReviewFinishListDto(
        Long customerHomePartyId,

        String partyInfo,

        LocalDateTime partySchedule,

        String chefName,

        String chefInfoExperience,

        String chefInfoIntroduce,

        Set<String> serviceName

) {
}