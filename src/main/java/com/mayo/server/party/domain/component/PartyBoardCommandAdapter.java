package com.mayo.server.party.domain.component;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.repository.ChefJpaRepository;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.DateUtility;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.repository.CustomerRepository;
import com.mayo.server.party.app.port.in.PartyBoardInputPort;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.domain.model.PartySchedule;
import com.mayo.server.party.domain.repository.PartyJpaScheduleRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
public class PartyBoardCommandAdapter implements PartyBoardInputPort {

    private final PartyJpaScheduleRepository partyJpaScheduleRepository;
    private final ChefJpaRepository chefRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void postPartySchedule(Long id, Long partyId, HomePartyDetailDto dto) {

        Chef chef = chefRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorCode.CHEF_NOT_FOUND));

        Customer customer = customerRepository.findById(dto.getCustomerHomeParty().getCustomer().getId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));

        PartySchedule partySchedule = PartySchedule
                .builder()
                .customer(customer)
                .chef(chef)
                .customerHomeParty(dto.getCustomerHomeParty())
                .isMatched(0)
                .createdAt(DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss))
                .build();

        partyJpaScheduleRepository.save(partySchedule);

    }
}
