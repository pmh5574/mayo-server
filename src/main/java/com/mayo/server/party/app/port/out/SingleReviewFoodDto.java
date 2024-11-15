package com.mayo.server.party.app.port.out;

import com.mayo.server.party.domain.enums.ReviewFoodReason;

public interface SingleReviewFoodDto {
    Long partyReviewFoodId();
    String getFoodReason();
}
