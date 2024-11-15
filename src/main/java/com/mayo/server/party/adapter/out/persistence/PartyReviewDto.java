package com.mayo.server.party.adapter.out.persistence;

import com.mayo.server.party.domain.enums.ReviewFoodReason;
import com.mayo.server.party.domain.enums.ReviewServicesReason;

import java.time.LocalDateTime;
import java.util.List;

public record PartyReviewDto (

        Long id,

        Integer rating,

        String ratingReason,

        String review,

        String createdAt,

        List<String> reviewFoodList,

        List<String> reviewServiceList
) {
}
