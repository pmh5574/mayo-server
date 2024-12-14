package com.mayo.server.party.app.service;

import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.PaginationDto;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.party.adapter.in.web.CustomerPartyChefRegisterRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyRegisterRequest;
import com.mayo.server.party.adapter.out.persistence.HomePartyFinishListResponse;
import com.mayo.server.party.adapter.out.persistence.HomePartyStatusResponse;
import com.mayo.server.party.app.port.in.CustomerPartyQueryInputPort;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import com.mayo.server.party.app.port.out.HomePartyNoReviewFinishListDto;
import com.mayo.server.party.app.port.out.HomePartyRegisterKitchenListDto;
import com.mayo.server.party.app.port.out.HomePartyStatusListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.PartySchedule;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerPartyService {

    private static final int MAX_PARTY_STATUS_FINISH_LIST_SIZE = 4;
    private static final int PAGE_SIZE = 8;
    private static final String SORT = "created_at";
    private static final Integer IS_MATCHED = 1;

    private final JwtService jwtService;
    private final CustomerPartyQueryInputPort customerPartyQueryInputPort;

    @Transactional
    public void savePartyToChef(final CustomerPartyChefRegisterRequest customerPartyChefRegisterRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Chef chef = customerPartyQueryInputPort.findByChef(customerPartyChefRegisterRequest.chefId());
        Kitchen kitchen = getByKitchen(customerPartyChefRegisterRequest.kitchenId(), userId);

        customerPartyQueryInputPort.postHomePartyToChef(customerPartyChefRegisterRequest, userId, chef, kitchen);
    }

    private Kitchen getByKitchen(final Long kitchenId, Long userId) {
        return customerPartyQueryInputPort.findByKitchen(kitchenId, userId);
    }

    @Transactional
    public void saveParty(final CustomerPartyRegisterRequest customerPartyRegisterRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Kitchen kitchen = getByKitchen(customerPartyRegisterRequest.kitchenId(), userId);

        customerPartyQueryInputPort.postHomeParty(customerPartyRegisterRequest, userId, kitchen);
    }

    public List<HomePartyStatusResponse> getPartyList(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        List<CustomerHomeParty> statusPartyList = customerPartyQueryInputPort.getStatusListByCustomerId(userId);

        if (statusPartyList.isEmpty()) {
            return null;
        }

        return convertToPartyResponseDto(statusPartyList);
    }

    private List<HomePartyStatusResponse> convertToPartyResponseDto(final List<CustomerHomeParty> statusPartyList) {
        return statusPartyList.stream()
                .collect(Collectors.groupingBy(
                        party -> mapToUnifiedStatus(party.getPartyStatus()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(CustomerHomeParty::getCreatedAt).reversed())
                                        .toList()
                        )
                )).entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals(HomePartyStatus.REJECTED))
                .map(entry -> {
                    HomePartyStatus status = entry.getKey();
                    List<HomePartyStatusListDto> partyDtoList = entry.getValue().stream()
                            .map(party -> new HomePartyStatusListDto(
                                    party.getCustomerHomePartyId(),
                                    party.getPartyInfo(),
                                    party.getBudget(),
                                    party.getPartySchedulesCount(),
                                    party.getPartySchedule(),
                                    party.getModifiedAt()))
                            .limit(status == HomePartyStatus.FINISH ? MAX_PARTY_STATUS_FINISH_LIST_SIZE : Long.MAX_VALUE)
                            .toList();

                    return new HomePartyStatusResponse(status, partyDtoList);
                }).toList();
    }

    private HomePartyStatus mapToUnifiedStatus(final HomePartyStatus status) {
        if (status == HomePartyStatus.CHEF_ACCEPTED) {
            return HomePartyStatus.ACCEPTED;
        }
        return status;
    }

    public HomePartyDetail getPartyDetail(final String homePartyId, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        List<HomePartyStatus> statuses = List.of(HomePartyStatus.CHEF_NOT_SELECTED, HomePartyStatus.WAITING, HomePartyStatus.ACCEPTED, HomePartyStatus.COMPLETED, HomePartyStatus.FINISH);

        return customerPartyQueryInputPort.getPartyDetail(homePartyId, userId, statuses);
    }

    public HomePartyFinishListResponse getFinishPartyList(
            final CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);

        HomePartyStatus finish = HomePartyStatus.FINISH;
        Long finishPartyListTotalCount = customerPartyQueryInputPort.getFinishPartyListTotalCount(
                customerPartyFinishSearchRequest.startDate(), customerPartyFinishSearchRequest.endDate(), userId,
                finish);

        List<HomePartyFinishListDto> finishPartyList = customerPartyQueryInputPort.getFinishPartyList(
                customerPartyFinishSearchRequest,
                userId,
                finish,
                new PaginationDto(customerPartyFinishSearchRequest.page(), PAGE_SIZE, SORT).getPageRequest());

        return new HomePartyFinishListResponse(finishPartyList, finishPartyListTotalCount);
    }

    public List<HomePartyRegisterKitchenListDto> getKitchenList(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        List<Kitchen> kitchenList = customerPartyQueryInputPort.getKitchenList(userId);

        if (kitchenList.isEmpty()) {
            return null;
        }

        return kitchenList.stream()
                .map(kitchen -> new HomePartyRegisterKitchenListDto(kitchen.getKitchenId(), kitchen.getNickName()))
                .toList();

    }

    public List<ChefNotSelectedDto> getChefNotSelectedList(final Long partyId, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        CustomerHomeParty customerHomeParty = checkChefNotSelectParty(partyId, userId);
        List<PartySchedule> partyScheduleList = getPartyScheduleListByCustomerId(customerHomeParty);

        if (partyScheduleList.isEmpty()) {
            return null;
        }

        return customerPartyQueryInputPort.getChefNotSelectedList(partyScheduleList);
    }

    private List<PartySchedule> getPartyScheduleListByCustomerId(final CustomerHomeParty customerHomeParty) {
        return customerPartyQueryInputPort.getChefIdList(customerHomeParty.getCustomerHomePartyId());
    }

    private CustomerHomeParty checkChefNotSelectParty(final Long partyId, final Long userId) {
        return customerPartyQueryInputPort.findByCustomerHomePartyReview(partyId,
                userId, HomePartyStatus.CHEF_NOT_SELECTED);
    }

    @Transactional
    public void postAssignChef(final Long partyScheduleId, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        PartySchedule partySchedule = customerPartyQueryInputPort.getPartySchedule(partyScheduleId, userId);
        checkMatched(partySchedule.getIsMatched());
        CustomerHomeParty customerHomeParty = partySchedule.getCustomerHomeParty();
        customerHomeParty.changeAssignChef(partySchedule);
    }

    private void checkMatched(final Integer isMatched) {
        if (Objects.equals(isMatched, IS_MATCHED)) {
            throw new DuplicateException(ErrorCode.PARTY_SCHEDULE_NOT_FOUND);
        }
    }

    public List<HomePartyNoReviewFinishListDto> getFinishPartyNoReviewList(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        return customerPartyQueryInputPort.getFinishPartyNoReviewList(userId, HomePartyStatus.FINISH);
    }
}
