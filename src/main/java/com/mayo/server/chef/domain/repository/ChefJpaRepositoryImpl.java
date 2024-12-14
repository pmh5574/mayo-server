package com.mayo.server.chef.domain.repository;

import static com.mayo.server.chef.domain.model.QChef.chef;
import static com.mayo.server.chef.domain.model.QChefHashTag.chefHashTag1;
import static com.mayo.server.chef.domain.model.QChefInformation.chefInformation;
import static com.mayo.server.party.domain.model.QPartySchedule.partySchedule;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.ChefNotSelectedHashTagDto;
import com.mayo.server.party.app.port.out.ChefSearch;
import com.mayo.server.party.app.port.out.ChefSearchHashTag;
import com.mayo.server.party.domain.model.PartySchedule;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChefJpaRepositoryImpl implements ChefJpaRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChefSearch> getSearchListAll(final List<String> categories, final List<String> services, final List<String> areas) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!services.isEmpty()) {
            services.forEach(service -> builder.or(chefHashTag1.chefHashTag.containsIgnoreCase(service)));
        }
        if (!categories.isEmpty()) {
            categories.forEach(category -> builder.or(chefHashTag1.chefHashTag.containsIgnoreCase(category)));
        }
        if (!areas.isEmpty()) {
            areas.forEach(area -> builder.or(chefInformation.chefInfoRegion.containsIgnoreCase(area)));
        }

        List<Long> chefIds = jpaQueryFactory
                .select(chef.id)
                .from(chef)
                .leftJoin(chefInformation).on(chef.id.eq(chefInformation.chefId))
                .leftJoin(chefHashTag1).on(chef.id.eq(chefHashTag1.chefId))
                .where(builder)
                .distinct()
                .fetch();

        return jpaQueryFactory
                .select(chef, chefInformation, chefHashTag1)
                .from(chef)
                .leftJoin(chefInformation).on(chef.id.eq(chefInformation.chefId))
                .leftJoin(chefHashTag1).on(chef.id.eq(chefHashTag1.chefId))
                .where(chef.id.in(chefIds))
                .transform(
                        groupBy(chef.id)
                                .list(
                                    Projections.constructor(ChefSearch.class,
                                        chef.id,
                                        chef.chefName,
                                        chefInformation.chefInfoExperience,
                                        chefInformation.chefInfoAdditional,
                                        chefInformation.hopePay,
                                        list(
                                            Projections.constructor(ChefSearchHashTag.class,
                                                chefHashTag1.id,
                                                chefHashTag1.chefHashTag
                                            )
                                        )
                                    )
                                )
                );
    }

    @Override
    public List<ChefNotSelectedDto> getChefNotSelectedList(final List<PartySchedule> partyScheduleList) {

        List<Chef> chefList = partyScheduleList.stream()
                .map(PartySchedule::getChef)
                .toList();

        return jpaQueryFactory
                .select(chef, chefInformation, chefHashTag1, partySchedule)
                .from(chef)
                .leftJoin(chefInformation).on(chef.id.eq(chefInformation.chefId))
                .leftJoin(chefHashTag1).on(chef.id.eq(chefHashTag1.chefId))
                .leftJoin(partySchedule).on(chef.id.eq(partySchedule.chef.id))
                .where(chef.in(chefList).and(partySchedule.in(partyScheduleList)))
                .transform(
                        groupBy(chef.id)
                                .list(
                                        Projections.constructor(ChefNotSelectedDto.class,
                                                chef.id,
                                                chef.chefName,
                                                chefInformation.chefInfoExperience,
                                                chefInformation.chefInfoAdditional,
                                                partySchedule.id.as("partyScheduleId"),
                                                list(
                                                        Projections.constructor(ChefNotSelectedHashTagDto.class,
                                                                chefHashTag1.id,
                                                                chefHashTag1.chefHashTag
                                                        )
                                                )
                                        )
                                )
                );
    }
}
