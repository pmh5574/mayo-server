package com.mayo.server.party.infra;

import static com.mayo.server.chef.domain.model.QChef.chef;
import static com.mayo.server.chef.domain.model.QChefInformation.chefInformation;
import static com.mayo.server.chef.domain.model.QChefPortfolio.chefPortfolio;
import static com.mayo.server.customer.domain.model.QKitchen.kitchen;
import static com.mayo.server.customer.domain.model.QKitchenImages.kitchenImages;
import static com.mayo.server.party.domain.model.QCustomerHomeParty.customerHomeParty;
import static com.mayo.server.party.domain.model.QCustomerHomePartyServices.customerHomePartyServices;
import static com.mayo.server.party.domain.model.QPartyReview.partyReview;
import static com.mayo.server.party.domain.model.QPartySchedule.partySchedule;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import com.mayo.server.common.Constants;
import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.app.port.out.HomePartyChefDetail;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import com.mayo.server.party.app.port.out.HomePartyKitchenDetail;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.QCustomerHomeParty;
import com.mayo.server.party.domain.repository.CustomerHomPartyRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomerHomePartyRepositoryImpl implements CustomerHomPartyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Deprecated
    @Override
    public PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyRequest req) {

        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<CustomerHomeParty> fetchQuery = queryFactory
                .select(qCustomerHomeParty)
                .distinct()
                .from(qCustomerHomeParty)
                .leftJoin(qCustomerHomeParty.partyServices, customerHomePartyServices)
                .fetchJoin()
                .where(qCustomerHomeParty.customerHomePartyId.in(getCustomerHomePartyIds(pageable, req)))
                .orderBy(qCustomerHomeParty.partySchedule.asc());

        List<CustomerHomeParty> results = fetchQuery.fetch();

        return new PartyBoardResponse.HomePartyPage(
                PartyBoardResponse.HomeParty.getHomeParties(results),
                Long.parseLong(String.valueOf(totalResults(req.days()).size()))
        );
    }

    @Override
    public PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyListRequest req, Long id) {

        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<CustomerHomeParty> fetchQuery = queryFactory
                .select(qCustomerHomeParty)
                .distinct()
                .from(qCustomerHomeParty)
                .leftJoin(qCustomerHomeParty.partyServices, customerHomePartyServices)
                .fetchJoin()
                .where(qCustomerHomeParty.partyStatus.eq(HomePartyStatus.CHEF_NOT_SELECTED).and(qCustomerHomeParty.customerHomePartyId.in(getCustomerHomePartyIds(pageable, id))))
                .orderBy(qCustomerHomeParty.partySchedule.asc());

        List<CustomerHomeParty> results = fetchQuery.fetch();

        return new PartyBoardResponse.HomePartyPage(
                PartyBoardResponse.HomeParty.getHomeParties(results),
                Long.parseLong(String.valueOf(totalResults().size()))
        );
    }

    @Deprecated
    public List<Long> getCustomerHomePartyIds(Pageable pageable, HomePartyRequest req) {
        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<Long> idQuery = queryFactory
                .select(qCustomerHomeParty.customerHomePartyId)
                .from(qCustomerHomeParty);

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : req.days()) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(qCustomerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        idQuery.where(conditions);

        idQuery.offset(pageable.getOffset());
        idQuery.limit(pageable.getPageSize());

        return idQuery.fetch();

    }

    public List<Long> getCustomerHomePartyIds(Pageable pageable, Long id) {
        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<Long> idQuery = queryFactory
                .select(qCustomerHomeParty.customerHomePartyId)
                .distinct()
                .from(qCustomerHomeParty)
                .leftJoin(qCustomerHomeParty.partyServices, customerHomePartyServices);

        BooleanBuilder conditions = new BooleanBuilder();

        conditions
                .and(customerHomeParty.partySchedule.gt(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)))
                .and(customerHomeParty.partyStatus.eq(HomePartyStatus.CHEF_NOT_SELECTED));

        idQuery.where(conditions);

        idQuery.offset(pageable.getOffset());
        idQuery.limit(pageable.getPageSize());

        return idQuery.fetch();

    }

    @Deprecated
    private List<Long> totalResults(List<String> dateStrings) {

        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<Long> idQuery = queryFactory
                .select(qCustomerHomeParty.customerHomePartyId)
                .from(qCustomerHomeParty);

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : dateStrings) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(qCustomerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        idQuery.where(conditions);

        return idQuery.fetch();
    }

    private List<Long> totalResults() {

        QCustomerHomeParty qCustomerHomeParty = customerHomeParty;

        JPAQuery<Long> idQuery = queryFactory
                .select(qCustomerHomeParty.customerHomePartyId)
                .distinct()
                .from(qCustomerHomeParty)
                .leftJoin(qCustomerHomeParty.partyServices, customerHomePartyServices);

        BooleanBuilder conditions = new BooleanBuilder();

        conditions
                .and(customerHomeParty.partySchedule.gt(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)))
                .and(customerHomeParty.partyStatus.eq(HomePartyStatus.CHEF_NOT_SELECTED));

        idQuery.where(conditions);

        return idQuery.fetch();
    }

    @Override
    public Optional<HomePartyDetail> getPartyDetail(final Long homePartyId, final List<HomePartyStatus> statuses,
                                                    final Long userId) {

        List<HomePartyDetail> homePartyDetails = queryFactory
                .select(customerHomeParty, customerHomePartyServices, chef, chefInformation, chefPortfolio, kitchen, kitchenImages)
                .from(customerHomeParty)
                .leftJoin(chef).on(customerHomeParty.chef.id.eq(chef.id))
                .leftJoin(customerHomePartyServices).on(customerHomeParty.eq(customerHomePartyServices.customerHomeParty))
                .leftJoin(chefPortfolio).on(chef.id.eq(chefPortfolio.chefId))
                .leftJoin(chefInformation).on(chef.id.eq(chefInformation.chefId))
                .leftJoin(kitchen).on(customerHomeParty.kitchen.eq(kitchen))
                .leftJoin(kitchenImages).on(kitchen.eq(kitchenImages.kitchen))
                .where(customerHomeParty.customerHomePartyId.eq(homePartyId)
                        .and(customerHomeParty.partyStatus.in(statuses))
                        .and(customerHomeParty.customer.id.eq(userId))
                        .and(chefPortfolio.deletedAt.isNull()))
                .transform(
                        groupBy(customerHomeParty.customerHomePartyId)
                                .list(Projections.constructor(HomePartyDetail.class,
                                        customerHomeParty.customerHomePartyId,
                                        customerHomeParty.partyStatus,
                                        customerHomeParty.partyInfo,
                                        customerHomeParty.budget,
                                        customerHomeParty.partySchedule,
                                        customerHomeParty.adultCount,
                                        customerHomeParty.childCount,
                                        customerHomeParty.partyComment,
                                        set(customerHomePartyServices.serviceName.stringValue()),
                                        Projections.constructor(HomePartyChefDetail.class,
                                                chef.chefName,
                                                chefInformation.chefInfoExperience,
                                                chefInformation.chefInfoIntroduce,
                                                chefInformation.chefInfoRegion,
                                                chefInformation.chefInfoAdditional,
                                                chefInformation.chefInfoDescription,
                                                set(chefPortfolio.chefPortfolioImage)
                                        ),
                                        Projections.constructor(HomePartyKitchenDetail.class,
                                                kitchen.nickName,
                                                kitchen.address,
                                                kitchen.addressSub,
                                                set(kitchenImages.imageName)
                                        )
                                ))
                );

        if (homePartyDetails.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(homePartyDetails.get(0));
    }

    @Override
    public List<Long> getMyWaitHomePartyIds(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {

        return this.getMyWaitHomePartyIds(chefId, dto.getPageRequest(), req.days());
    }

    private List<Long> getMyWaitHomePartyIds(Long chefId, Pageable pageable, List<String> days) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.customerHomeParty.customerHomePartyId)
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : days) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(customerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        conditions.and(customerHomeParty.partySchedule.goe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)));

        conditions.and((
                partySchedule.chef.id.eq(chefId)
                .and(
                        partySchedule.isMatched.eq(0)
                )
                        .and(
                                customerHomeParty.partyStatus.in(
                                        HomePartyStatus.WAITING,
                                        HomePartyStatus.ACCEPTED
                                )
                        )
        ));

        idQuery.where(conditions);

        idQuery.offset(pageable.getOffset());
        idQuery.limit(pageable.getPageSize());

        return idQuery.fetch();

    }

    @Override
    public List<Long> getMyMatchedHomePartyIds(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return this.getMyMatchedHomePartyIds(chefId, dto.getPageRequest(), req.days());
    }

    private List<Long> getMyMatchedHomePartyIds(Long chefId, Pageable pageable, List<String> days) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.customerHomeParty.customerHomePartyId)
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : days) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(customerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        conditions.and(customerHomeParty.partySchedule.goe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)));

        conditions.and((
                partySchedule.chef.id.eq(chefId)
                        .and(
                                partySchedule.isMatched.eq(1)
                        )
                        .and(
                                customerHomeParty.partyStatus.in(HomePartyStatus.COMPLETED)
                        )
        ));

        idQuery.where(conditions);

        idQuery.offset(pageable.getOffset());
        idQuery.limit(pageable.getPageSize());

        return idQuery.fetch();

    }

    @Override
    public List<Long> getMyFinishedHomePartyIds(Long chefId, PaginationWithTimeDto dto) {
        return this.getMyFinishedHomePartyIds(chefId, dto.getPageRequest());
    }

    private List<Long> getMyFinishedHomePartyIds(Long chefId, Pageable pageable) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.customerHomeParty.customerHomePartyId)
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();

        conditions.and(customerHomeParty.partySchedule.loe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)));

        conditions.and((
                partySchedule.chef.id.eq(chefId)
                        .and(
                                partySchedule.isMatched.eq(1)
                        )
        ));

        idQuery.where(conditions);

        idQuery.offset(pageable.getOffset());
        idQuery.limit(pageable.getPageSize());

        return idQuery.fetch();

    }

    @Override
    public Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.count())
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : req.days()) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(customerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        conditions.and(customerHomeParty.partySchedule.goe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)));

        conditions.and((
                partySchedule.chef.id.eq(chefId)
                        .and(
                                partySchedule.isMatched.eq(0)
                        )
                        .and(
                                customerHomeParty.partyStatus.in(
                                        HomePartyStatus.WAITING,
                                        HomePartyStatus.ACCEPTED
                                )
                        )
        ));

        idQuery.where(conditions);

        return idQuery.fetchOne();
    }

    @Override
    public Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.count())
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();
        for (String day : req.days()) {
            LocalDateTime date = DateUtility.parsedStringToLocaleDate(day, Constants.yyyy_MM_DD_HH_mm_ss);
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

            conditions.or(customerHomeParty.partySchedule.between(startOfDay, endOfDay));
        }

        conditions.and(customerHomeParty.partySchedule.goe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)));

        conditions.and((
                partySchedule.chef.id.eq(chefId)
                        .and(
                                partySchedule.isMatched.eq(1)
                        )
                        .and(
                                customerHomeParty.partyStatus.in(HomePartyStatus.COMPLETED)
                        )
        ));

        idQuery.where(conditions);

        return idQuery.fetchOne();
    }

    @Override
    public Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto) {

        JPAQuery<Long> idQuery = queryFactory
                .select(partySchedule.count())
                .from(partySchedule)
                .leftJoin(customerHomeParty).on(partySchedule.customerHomeParty.customerHomePartyId.eq(customerHomeParty.customerHomePartyId))
                .fetchJoin();

        BooleanBuilder conditions = new BooleanBuilder();

        conditions.and(customerHomeParty.partySchedule.loe(DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss)).and(partySchedule.chef.id.eq(chefId)).and(partySchedule.isMatched.eq(1)));

        idQuery.where(conditions);

        return idQuery.fetchOne();
    }

    @Override
    public List<HomePartyFinishListDto> getFinishPartyList(final CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest,
                                                           final Long userId,
                                                           final HomePartyStatus status,
                                                           final Pageable pageable) {
        LocalDateTime startDate = DateUtility.parsedStringToLocaleDate(customerPartyFinishSearchRequest.startDate() + "000000", Constants.yyyyMMDDHHmmss);
        LocalDateTime endDate = DateUtility.parsedStringToLocaleDate(customerPartyFinishSearchRequest.endDate() + "235959", Constants.yyyyMMDDHHmmss);
        return queryFactory.select(
                        Projections.constructor(HomePartyFinishListDto.class,
                                customerHomeParty.customerHomePartyId.as("id"),
                                customerHomeParty.partyInfo,
                                customerHomeParty.modifiedAt,
                                partyReview.isNotNull().as("hasReview")
                        ))
                .from(customerHomeParty)
                .leftJoin(partyReview).on(partyReview.customerHomeParty.eq(customerHomeParty))
                .where(customerHomeParty.customer.id.eq(userId)
                        .and(customerHomeParty.partyStatus.eq(status))
                        .and(customerHomeParty.partySchedule.between(startDate, endDate))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<CustomerHomeParty> getStatusListByCustomerId(final Long userId) {
        return queryFactory
                .selectFrom(customerHomeParty)
                .leftJoin(customerHomeParty.partySchedules, partySchedule).fetchJoin()
                .where(customerHomeParty.customer.id.eq(userId))
                .orderBy(customerHomeParty.createdAt.desc())
                .fetch();
    }
}
