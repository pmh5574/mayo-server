package com.mayo.server.party.adapter.out.persistence;

import com.mayo.server.customer.app.port.out.SingleKitchenImageDto;
import com.mayo.server.customer.domain.model.KitchenImages;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PartyApplyResponse {

    public record ApplyHomaParty(

            Long id,

            String info,

            String address,

            String comment,

            BigDecimal budget,

            LocalDateTime scheduleAt,

            Integer numberOfPeople,

            Integer adult,

            Integer child,

            List<String> serviceList,

            String burnerType,

            List<SingleKitchenImageDto> kitchenImages,

            List<String> kitchenTools,

            String kitchenRequirements,

            String kitchenConsideration
    ) {

    }

    public record ApplyList(

            Long id,

            String info,

            String address,

            String scheduleAt,

            String createdAt,

            BigDecimal budget,

            String capacity
    ) {

    }

    public record MatchingWaitingList(

            Long id,

            String info,

            String scheduleAt
    ) {}

    public record MatchingList(

            Long id,

            String info,

            String scheduleAt
    ) {}

    public record VisitList(

            Long id,

            String info,

            String scheduleAt
    ) {}

    public record HomePartyReview(

            String review,

            String createdAt,

            List<String> reviewServiceList,

            List<String> reviewFoodReasonList
    ) { }
}
