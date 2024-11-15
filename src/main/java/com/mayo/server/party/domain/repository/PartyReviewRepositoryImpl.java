package com.mayo.server.party.domain.repository;

import static com.mayo.server.chef.domain.model.QChef.chef;
import static com.mayo.server.party.domain.model.QCustomerHomeParty.customerHomeParty;
import static com.mayo.server.party.domain.model.QPartyReview.partyReview;
import static com.mayo.server.party.domain.model.QPartyReviewFood.partyReviewFood;
import static com.mayo.server.party.domain.model.QPartyReviewServices.partyReviewServices;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import com.mayo.server.party.app.port.out.PartyReviewListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PartyReviewRepositoryImpl implements PartyReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PartyReviewListDto> getReviewList(final Long userId) {
        return jpaQueryFactory
                .select(partyReview, partyReviewFood, partyReviewServices, customerHomeParty, chef)
                .from(partyReview)
                .leftJoin(partyReviewFood).on(partyReview.eq(partyReviewFood.partyReview))
                .leftJoin(partyReviewServices).on(partyReview.eq(partyReviewServices.partyReview))
                .leftJoin(customerHomeParty).on(partyReview.customerHomeParty.eq(customerHomeParty))
                .leftJoin(chef).on(customerHomeParty.chef.eq(chef))
                .where(customerHomeParty.customer.id.eq(userId))
                .transform(
                        groupBy(partyReview.partyReviewId)
                                .list(
                                        Projections.constructor(PartyReviewListDto.class,
                                                partyReview.partyReviewId,
                                                customerHomeParty.customerHomePartyId,
                                                chef.chefName,
                                                partyReview.reviewContent,
                                                partyReview.createdAt,
                                                set(Expressions.asString(partyReviewFood.foodReason.stringValue())),
                                                set(Expressions.asString(partyReviewServices.serviceReason.stringValue()))
                                        )
                                )
                );
    }
}
