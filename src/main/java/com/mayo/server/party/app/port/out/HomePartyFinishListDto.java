package com.mayo.server.party.app.port.out;

import java.time.LocalDateTime;

public record HomePartyFinishListDto(
        Long id,

        String partyInfo,

        LocalDateTime modifiedAt,

        Boolean hasReview
) {
}