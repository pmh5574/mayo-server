package com.mayo.server.party.app.port.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HomePartyStatusListDto(
        Long id,
        String partyInfo,
        BigDecimal budget,
        Integer chefCount,
        LocalDateTime partySchedule,
        LocalDateTime modifiedAt
) {
    public String getId() {
        return id.toString();
    }
}
