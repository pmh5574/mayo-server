package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.SingleReviewServiceDto;
import com.mayo.server.party.domain.model.PartyReviewServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartyReviewServicesRepository extends JpaRepository<PartyReviewServices, Long> {

    @Query(
            """
        SELECT prs.partyReviewServiceId AS partyReviewServiceId,
            CONCAT('', prs.serviceReason) AS serviceReason
         FROM PartyReviewServices AS prs WHERE prs.partyReview.partyReviewId = :reviewId
    """
    )
    List<SingleReviewServiceDto> findAllByPartyReview_PartyReviewId(@Param("reviewId") Long reviewId);

}
