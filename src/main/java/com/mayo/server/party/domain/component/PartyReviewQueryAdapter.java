package com.mayo.server.party.domain.component;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.party.adapter.in.web.PartyReviewRegisterRequest;
import com.mayo.server.party.app.port.in.CustomerPartyQueryInputPort;
import com.mayo.server.party.app.port.in.PartyReviewQueryInputPort;
import com.mayo.server.party.app.port.out.PartyReviewListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.enums.ReviewFoodReason;
import com.mayo.server.party.domain.enums.ReviewServicesReason;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartyReview;
import com.mayo.server.party.domain.model.PartyReviewFood;
import com.mayo.server.party.domain.model.PartyReviewServices;
import com.mayo.server.party.domain.repository.PartyReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Adapter
public class PartyReviewQueryAdapter implements PartyReviewQueryInputPort {

    private final PartyReviewRepository partyReviewRepository;
    private final CustomerPartyQueryInputPort customerPartyQueryInputPort;

    @Override
    public Long saveReview(final PartyReviewRegisterRequest partyReviewRegisterRequest, final CustomerHomeParty customerHomeParty) {
        PartyReview partyReview = partyReviewRepository.save(PartyReview.builder()
                .ratingScore(partyReviewRegisterRequest.ratingScore())
                .ratingReason(partyReviewRegisterRequest.ratingReason())
                .reviewContent(partyReviewRegisterRequest.reviewContent())
                .build());

        partyReview.addCustomerHomeParty(customerHomeParty);

        if (!partyReviewRegisterRequest.serviceList().isEmpty()) {
            partyReview.addServiceList(postServices(partyReviewRegisterRequest.serviceList(), partyReview));
        }

        if (!partyReviewRegisterRequest.foodList().isEmpty()) {
            partyReview.addFoodList(postFoodList(partyReviewRegisterRequest.foodList(), partyReview));
        }

        return partyReview.getPartyReviewId();
    }

    private List<PartyReviewServices> postServices(final List<ReviewServicesReason> reviewServiceReasons,
                                                   final PartyReview partyReview) {
        return reviewServiceReasons.stream()
                .map(reviewServicesReason -> PartyReviewServices.builder()
                        .reviewServicesReason(reviewServicesReason)
                        .partyReview(partyReview)
                        .build())
                .toList();
    }

    private List<PartyReviewFood> postFoodList(final List<ReviewFoodReason> reviewFoodReasons, final PartyReview partyReview) {
        return reviewFoodReasons.stream()
                .map(reviewFoodReason -> PartyReviewFood.builder()
                        .foodReason(reviewFoodReason)
                        .partyReview(partyReview)
                        .build())
                .toList();
    }

    @Override
    public CustomerHomeParty findByCustomerHomeParty(final Long homePartyId, final Long userId,
                                                     final HomePartyStatus status) {
        return customerPartyQueryInputPort.findByCustomerHomePartyReview(homePartyId, userId, status);
    }

    @Override
    public Boolean checkReviewExists(final CustomerHomeParty customerHomeParty) {
        return partyReviewRepository.findByCustomerHomeParty(customerHomeParty)
                .isPresent();
    }

    @Override
    public List<PartyReviewListDto> getReviewList(final Long userId) {
        return partyReviewRepository.getReviewList(userId);
    }
}
