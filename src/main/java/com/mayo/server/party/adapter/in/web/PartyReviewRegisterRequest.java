package com.mayo.server.party.adapter.in.web;

import com.mayo.server.party.domain.enums.ReviewFoodReason;
import com.mayo.server.party.domain.enums.ReviewServicesReason;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PartyReviewRegisterRequest(

        @NotNull(message = "잘못된 요청입니다.")
        Long homePartyId,

        @NotNull(message = "평점을 입력해 주세요.")
        @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
        @Max(value = 100, message = "평점은 100점 이하이어야 합니다.")
        Integer ratingScore,

        String ratingReason,

        String reviewContent,

        @NotNull(message = "서비스 관련해서 좋았던 부분을 선택해주세요.")
        List<ReviewServicesReason> serviceList,

        @NotNull(message = "음식 관련해서 좋았던 부분을 선택해주세요.")
        List<ReviewFoodReason> foodList
) {
}