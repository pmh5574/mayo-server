package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.SingleReviewDto;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartyReview;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartyReviewRepository extends JpaRepository<PartyReview, Long>, PartyReviewRepositoryCustom {

    Optional<PartyReview> findByCustomerHomeParty(CustomerHomeParty customerHomeParty);

    @Query("""
    SELECT new com.mayo.server.party.app.port.out.SingleReviewDto(
        pr.partyReviewId,
        pr.ratingScore,
        pr.ratingReason,
        pr.reviewContent,
        pr.createdAt
    ) FROM PartyReview AS pr WHERE pr.customerHomeParty.customerHomePartyId = :customerHomePartyId
    """)
    Optional<SingleReviewDto> findByCustomerHomeParty_CustomerHomePartyId(@Param("customerHomePartyId") Long customerHomePartyId);
}