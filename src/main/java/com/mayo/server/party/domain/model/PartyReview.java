package com.mayo.server.party.domain.model;

import static jakarta.persistence.FetchType.LAZY;
import com.mayo.server.common.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PartyReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyReviewId;

    private Integer ratingScore;

    private String ratingReason;

    private String reviewContent;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "home_party_id")
    private CustomerHomeParty customerHomeParty;

    @OneToMany(mappedBy = "partyReview", cascade = CascadeType.ALL)
    private List<PartyReviewServices> partyReviewServicesList = new ArrayList<>();

    @OneToMany(mappedBy = "partyReview", cascade = CascadeType.ALL)
    private List<PartyReviewFood> partyReviewFoodList = new ArrayList<>();

    @Builder
    public PartyReview(final Integer ratingScore, final String ratingReason, final String reviewContent) {
        this.ratingScore = ratingScore;
        this.ratingReason = ratingReason;
        this.reviewContent = reviewContent;
    }

    public void addCustomerHomeParty(final CustomerHomeParty customerHomeParty) {
        this.customerHomeParty = customerHomeParty;
    }

    public void addServiceList(final List<PartyReviewServices> partyReviewServices) {
        this.partyReviewServicesList = partyReviewServices;
    }

    public void addFoodList(final List<PartyReviewFood> partyReviewFoods) {
        this.partyReviewFoodList = partyReviewFoods;
    }
}
