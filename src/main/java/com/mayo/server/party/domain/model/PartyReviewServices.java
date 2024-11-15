package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;
import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.party.domain.enums.ReviewServicesReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PartyReviewServices extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_review_service_id")
    private Long partyReviewServiceId;

    @Enumerated(EnumType.STRING)
    private ReviewServicesReason serviceReason;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_review_id")
    private PartyReview partyReview;

    @Builder
    public PartyReviewServices(final ReviewServicesReason reviewServicesReason, final PartyReview partyReview) {
        this.serviceReason = reviewServicesReason;
        this.partyReview = partyReview;
    }
}
