package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.SingleReviewFoodDto;
import com.mayo.server.party.domain.model.PartyReviewFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyReviewFoodRepository extends JpaRepository<PartyReviewFood, Long> {

    @Query("""
            SELECT prf.partyReviewFoodId AS partyReviewFoodId,
                CONCAT('', prf.foodReason) AS foodReason
            FROM PartyReviewFood AS prf WHERE prf.partyReview.partyReviewId = :partyReviewId
        """)
    Optional<List<SingleReviewFoodDto>> findAllByPartyReview_PartyReviewId(@Param("partyReviewId") Long partyReviewId);
}
