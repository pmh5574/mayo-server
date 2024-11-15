package com.mayo.server.party.domain.component;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.app.port.in.KitchenQueryInputPort;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.party.adapter.in.web.CustomerPartyChefRegisterRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyRegisterRequest;
import com.mayo.server.party.app.port.in.ChefPartyQueryInputPort;
import com.mayo.server.party.app.port.in.CustomerPartyQueryInputPort;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import com.mayo.server.party.domain.enums.CustomerPartyServices;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.model.CustomerHomePartyServices;
import com.mayo.server.party.domain.model.PartySchedule;
import com.mayo.server.party.domain.repository.CustomerHomePartyRepository;
import com.mayo.server.party.domain.repository.PartyJpaScheduleRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Adapter
public class CustomerPartyQueryAdapter implements CustomerPartyQueryInputPort {

    private final CustomerHomePartyRepository customerHomePartyRepository;
    private final CustomerQueryInputPort customerQueryInputPort;
    private final ChefPartyQueryInputPort chefPartyQueryInputPort;
    private final KitchenQueryInputPort kitchenQueryInputPort;
    private final PartyJpaScheduleRepository partyJpaScheduleRepository;

    @Override
    public Long postHomePartyToChef(final CustomerPartyChefRegisterRequest customerPartyChefRegisterRequest, final Long customerId,
                                    final Chef chef, final Kitchen kitchen) {

        Customer customer = getCustomer(customerId);

        LocalDateTime localDateTime = stringToLocalDateTime(customerPartyChefRegisterRequest.partySchedule());

        CustomerHomeParty customerHomeParty = customerHomePartyRepository.save(CustomerHomeParty.builder()
                .partyStatus(HomePartyStatus.WAITING)
                .partyInfo(customerPartyChefRegisterRequest.partyInfo())
                .budget(customerPartyChefRegisterRequest.budget())
                .partySchedule(localDateTime)
                .adultCount(customerPartyChefRegisterRequest.adultCount())
                .childCount(customerPartyChefRegisterRequest.childCount())
                .partyComment(customerPartyChefRegisterRequest.partyComment())
                .customer(customer)
                .build());

        customerHomeParty.addChef(chef);
        customerHomeParty.addKitchen(kitchen);

        if (!customerPartyChefRegisterRequest.partyServices().isEmpty()) {
            customerHomeParty.addHomePartyServices(postHomePartyServices(customerPartyChefRegisterRequest.partyServices(), customerHomeParty));
        }

        return customerHomeParty.getCustomerHomePartyId();
    }

    private static LocalDateTime stringToLocalDateTime(final String partySchedule) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(partySchedule, formatter);
    }

    private List<CustomerHomePartyServices> postHomePartyServices(final List<CustomerPartyServices> customerPartyServices,
                                                                  final CustomerHomeParty customerHomeParty) {
        return customerPartyServices.stream()
                .map(customerPartyService -> CustomerHomePartyServices.builder()
                        .serviceName(customerPartyService)
                        .customerHomeParty(customerHomeParty)
                        .build())
                .toList();
    }

    @Override
    public Chef findByChef(final Long chefId) {
        return chefPartyQueryInputPort.findByChef(chefId);
    }

    @Override
    public Long postHomeParty(final CustomerPartyRegisterRequest customerPartyRegisterRequest, final Long customerId,
                              final Kitchen kitchen) {
        Customer customer = getCustomer(customerId);

        LocalDateTime localDateTime = stringToLocalDateTime(customerPartyRegisterRequest.partySchedule());

        CustomerHomeParty customerHomeParty = customerHomePartyRepository.save(CustomerHomeParty.builder()
                .partyStatus(HomePartyStatus.CHEF_NOT_SELECTED)
                .partyInfo(customerPartyRegisterRequest.partyInfo())
                .budget(customerPartyRegisterRequest.budget())
                .partySchedule(localDateTime)
                .adultCount(customerPartyRegisterRequest.adultCount())
                .childCount(customerPartyRegisterRequest.childCount())
                .partyComment(customerPartyRegisterRequest.partyComment())
                .customer(customer)
                .build());

        customerHomeParty.addKitchen(kitchen);

        if (!customerPartyRegisterRequest.partyServices().isEmpty()) {
            customerHomeParty.addHomePartyServices(postHomePartyServices(customerPartyRegisterRequest.partyServices(), customerHomeParty));
        }

        return customerHomeParty.getCustomerHomePartyId();
    }

    private Customer getCustomer(final Long customerId) {
        return customerQueryInputPort.findById(customerId);
    }

    @Override
    public List<CustomerHomeParty> getStatusListByCustomerId(final Long userId) {
        return customerHomePartyRepository.getStatusListByCustomerId(userId);
//        return customerHomePartyRepository.findByCustomerId(userId);
    }

    @Override
    public HomePartyDetail getPartyDetail(final String homePartyId, final Long userId,
                                            final List<HomePartyStatus> statuses) {
        return customerHomePartyRepository.getPartyDetail(Long.parseLong(homePartyId), statuses, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PARTY_NOT_FOUND));
    }

    @Override
    public CustomerHomeParty findByCustomerHomePartyReview(final Long homePartyId, final Long userId,
                                                           final HomePartyStatus status) {
        return customerHomePartyRepository.findByCustomerHomePartyIdAndPartyStatusAndCustomerId(homePartyId, status, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PARTY_NOT_FOUND));
    }

    @Override
    public List<HomePartyFinishListDto> getFinishPartyList(final CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest,
                                                           final Long userId, final HomePartyStatus status,
                                                           final Pageable pageable) {
        return customerHomePartyRepository.getFinishPartyList(customerPartyFinishSearchRequest, userId, status, pageable);
    }

    @Override
    public Kitchen findByKitchen(final Long kitchenId, final Long customerId) {
        return kitchenQueryInputPort.findByKitchenAndCustomerId(kitchenId, customerId);
    }

    @Override
    public List<Kitchen> getKitchenList(final Long userId) {
        return kitchenQueryInputPort.findKitchenListByCustomerId(userId);
    }

    @Override
    public List<PartySchedule> getChefIdList(final Long customerHomePartyId) {
        return partyJpaScheduleRepository.findAllByCustomerHomePartyCustomerHomePartyId(customerHomePartyId);
    }

    @Override
    public List<ChefNotSelectedDto> getChefNotSelectedList(final List<PartySchedule> partyScheduleList) {
        return chefPartyQueryInputPort.getChefNotSelectedList(partyScheduleList);
    }

    @Override
    public PartySchedule getPartySchedule(final Long partyScheduleId, final Long userId) {
        return partyJpaScheduleRepository.findByIdAndCustomerId(partyScheduleId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PARTY_SCHEDULE_NOT_FOUND));
    }
}
