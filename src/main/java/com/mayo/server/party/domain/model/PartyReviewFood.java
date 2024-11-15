package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;
import com.mayo.server.common.BaseTimeEntity;
import com.mayo.server.party.domain.enums.ReviewFoodReason;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PartyReviewFood extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "party_review_food")
    private Long partyReviewFoodId;

    @Enumerated(EnumType.STRING)
    private ReviewFoodReason foodReason;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_review_id")
    private PartyReview partyReview;

    @Builder
    public PartyReviewFood(final ReviewFoodReason foodReason, final PartyReview partyReview) {
        this.foodReason = foodReason;
        this.partyReview = partyReview;
    }
}