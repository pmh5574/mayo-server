package com.mayo.server.party.domain.component;

import com.mayo.server.common.Constants;
import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.customer.app.port.out.SingleKitchenDto;
import com.mayo.server.customer.app.port.out.SingleKitchenImageDto;
import com.mayo.server.customer.app.port.out.SingleKitchenToolsDto;
import com.mayo.server.customer.domain.repository.KitchenImagesRepository;
import com.mayo.server.customer.domain.repository.KitchenRepository;
import com.mayo.server.customer.domain.repository.KitchenToolsRepository;
import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.PartyApplyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.app.port.out.MyHomePartyDto;
import com.mayo.server.party.app.port.out.PartyOutputPort;
import com.mayo.server.party.app.port.out.SingleReviewDto;
import com.mayo.server.party.app.port.out.SingleReviewFoodDto;
import com.mayo.server.party.app.port.out.SingleReviewServiceDto;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartySchedule;
import com.mayo.server.party.domain.repository.CustomerHomePartyRepository;
import com.mayo.server.party.domain.repository.PartyJpaScheduleRepository;
import com.mayo.server.party.domain.repository.PartyReviewFoodRepository;
import com.mayo.server.party.domain.repository.PartyReviewRepository;
import com.mayo.server.party.domain.repository.PartyReviewServicesRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Adapter
@RequiredArgsConstructor
public class PartyQueryAdapter implements PartyOutputPort {

    private final CustomerHomePartyRepository customerHomePartyRepository;
    private final PartyJpaScheduleRepository partyJpaScheduleRepository;
    private final KitchenRepository kitchenRepository;
    private final KitchenToolsRepository kitchenToolsRepository;
    private final KitchenImagesRepository kitchenImagesRepository;
    private final PartyReviewRepository partyReviewRepository;
    private final PartyReviewFoodRepository partyReviewFoodRepository;
    private final PartyReviewServicesRepository partyReviewServicesRepository;

    @Override
    public PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyRequest req) {

        return customerHomePartyRepository.getCustomerHomeParties(pageable, req);
    }

    @Override
    public PartyBoardResponse.HomePartyPage getCustomerHomeParties(Pageable pageable, HomePartyListRequest req, Long id) {
        return customerHomePartyRepository.getCustomerHomeParties(pageable, req, id);
    }

    @Override
    public HomePartyDetailDto getCustomerHomeParty(Long customerHomePartyId) {

        CustomerHomeParty customerHomeParty = customerHomePartyRepository.findByCustomerHomePartyId(customerHomePartyId);
        List<SingleKitchenDto> kitchen = kitchenRepository.findAllByCustomer_Id(customerHomeParty.getCustomer().getId());
        if(kitchen.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.KITCHEN_NOT_FOUND,
                    ErrorCode.KITCHEN_NOT_FOUND.getMessage()
            );
        };
        SingleKitchenDto homePartyKitchen = kitchen.get(0);
        List<SingleKitchenToolsDto> kitchenTools = kitchenToolsRepository.findAllByKitchen_Id(homePartyKitchen.kitchenId());
        List<SingleKitchenImageDto> kitchenImages = kitchenImagesRepository.findAllByKitchen_KitchenId(homePartyKitchen.kitchenId());
        return new HomePartyDetailDto(
                customerHomeParty,
                homePartyKitchen.address(),
                homePartyKitchen.burnerType(),
                kitchenImages,
                homePartyKitchen.requirements(),
                homePartyKitchen.considerations(),
                kitchenTools.stream().map(SingleKitchenToolsDto::toolName).toList()
        );
    }

    @Override
    public boolean existMatchedHomeParty(Long id) {

        List<PartySchedule> schedules = partyJpaScheduleRepository.findAllByCustomerHomePartyCustomerHomePartyId(id);

        for (PartySchedule schedule : schedules) {
            if (schedule.getIsMatched() == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<PartyApplyResponse.ApplyList> getApplyList(Long id) {

        List<CustomerHomeParty> partySchedule = customerHomePartyRepository.findAllByChef_Id(id);

        return partySchedule.stream().map(
                v -> {

                    List<SingleKitchenDto> kitchen = kitchenRepository.findAllByCustomer_Id(v.getCustomer().getId());
                    if(kitchen.isEmpty()) {
                        throw new InvalidRequestException(
                                ErrorCode.KITCHEN_NOT_FOUND,
                                ErrorCode.KITCHEN_NOT_FOUND.getMessage()
                        );
                    };

                    return new PartyApplyResponse.ApplyList(
                            v.getCustomerHomePartyId(),
                            v.getPartyInfo(),
                            kitchen.get(0).address(),
                            v.getPlainSchedule(),
                            v.getPlainCreatedAt(),
                            v.getBudget(),
                            ("어른: " + v.getAdultCount() + "어린이" + v.getChildCount())
                    );
                }
        ).toList();
    }

    @Override
    public List<PartyApplyResponse.MatchingWaitingList> getMatchingWaitingList(Long id, PaginationWithTimeDto dto) {

        return partyJpaScheduleRepository.findPendingPartiesByChefId(id, dto.getUTCPlainStart(), dto.getUTCPlainEnd(), dto.getPageRequest()).stream().map(v ->
                new PartyApplyResponse.MatchingWaitingList(
                v.id(),
                v.info(),
                v.getPlainSchedule()
        )).toList();
    }

    @Override
    public List<PartyApplyResponse.MatchingList> getMatchedList(Long id,PaginationWithTimeDto dto) {
        return partyJpaScheduleRepository.findCompletedPartiesByChefId(id, DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss), dto.getPageRequest()).stream().map(v ->
                new PartyApplyResponse.MatchingList(
                v.id(),
                v.info(),
                v.getPlainSchedule())).toList();
    }

    @Override
    public List<PartyApplyResponse.VisitList> getVisitList(Long id,PaginationWithTimeDto dto) {
        return partyJpaScheduleRepository.findPartiesByChefIdAndScheduleBefore(
                id,
                DateUtility.getLocalUTC0String(Constants.yyyy_MM_DD_HH_mm_ss),
                dto.getPageRequest()
        ).stream().map(v ->
                new PartyApplyResponse.VisitList(
                        v.id(),
                        v.info(),
                        v.getPlainSchedule())).toList();
    }

    @Override
    public List<MyHomePartyDto> getMyWaitHomePartyList(Long id, PaginationWithTimeDto dto, MyHomePartyListRequest req) {

        List<Long> ids = customerHomePartyRepository.getMyWaitHomePartyIds(id, dto, req);

        return customerHomePartyRepository.findAllByCustomerHomePartyId(ids);
    }

    @Override
    public List<MyHomePartyDto> getMyMatchedHomePartyList(Long id, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        List<Long> ids = customerHomePartyRepository.getMyMatchedHomePartyIds(id, dto, req);

        return customerHomePartyRepository.findAllByCustomerHomePartyId(ids);
    }

    @Override
    public List<MyHomePartyDto> getMyFinishedHomePartyList(Long id, PaginationWithTimeDto dto) {
        List<Long> ids = customerHomePartyRepository.getMyFinishedHomePartyIds(id, dto);

        return customerHomePartyRepository.findAllByCustomerHomePartyId(ids);
    }

    @Override
    public Long getMyWaitCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return customerHomePartyRepository.getMyWaitCount(chefId, dto, req);
    }

    @Override
    public Long getMyMatchedCount(Long chefId, PaginationWithTimeDto dto, MyHomePartyListRequest req) {
        return customerHomePartyRepository.getMyMatchedCount(chefId, dto, req);
    }

    @Override
    public Long getMyFinishedCount(Long chefId, PaginationWithTimeDto dto) {
        return customerHomePartyRepository.getMyFinishedCount(chefId, dto);
    }

    @Override
    public PartyReviewDto getHomePartyReview(Long homePartyId) {

        Optional<SingleReviewDto> dto = partyReviewRepository.findByCustomerHomeParty_CustomerHomePartyId(homePartyId);
        if(dto.isEmpty()) {
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        List<SingleReviewFoodDto> reviewFoodList = partyReviewFoodRepository.findAllByPartyReview_PartyReviewId(dto.get().partyReviewId()).orElse(List.of());
        List<SingleReviewServiceDto> reviewServiceList = partyReviewServicesRepository.findAllByPartyReview_PartyReviewId(dto.get().partyReviewId());

        return new PartyReviewDto(
                dto.get().partyReviewId(),
                dto.get().ratingScore(),
                dto.get().ratingReason(),
                dto.get().reviewContent(),
                DateUtility.replacePlainTimeFromLocaleDate(dto.get().createdAt()),
                reviewFoodList.isEmpty() ? List.of() : reviewFoodList.stream().map(SingleReviewFoodDto::getFoodReason).toList(),
                reviewServiceList.isEmpty() ? List.of() : reviewServiceList.stream().map(SingleReviewServiceDto::getServiceReason).toList()
        );
    }

    public static Specification<CustomerHomeParty> getBetween(LocalDateTime startAt, LocalDateTime endAt) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("partySchedule"), startAt, endAt);
    }
}
