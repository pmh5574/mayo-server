package com.mayo.server.party.app.port.out;

import java.time.LocalDateTime;

public record SingleReviewDto (

        Long partyReviewId,

        Integer ratingScore,

        String ratingReason,

        String reviewContent,

        LocalDateTime createdAt

) {
}
