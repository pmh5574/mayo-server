package com.mayo.server.payment.domain.component;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import com.mayo.server.party.domain.repository.CustomerHomePartyRepository;
import com.mayo.server.payment.app.port.in.PayCommand;
import com.mayo.server.payment.domain.models.Event;
import com.mayo.server.payment.domain.models.Pay;
import com.mayo.server.payment.domain.models.PayHomeParty;
import com.mayo.server.payment.domain.repository.EventRepository;
import com.mayo.server.payment.domain.repository.PayHomePartyRepository;
import com.mayo.server.payment.domain.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class PayCommandAdapter implements PayCommand {

    private final EventRepository eventRepository;
    private final PayRepository payRepository;
    private final PayHomePartyRepository payHomePartyRepository;
    private final CustomerHomePartyRepository customerHomePartyRepository;

    @Override
    public void save(Event event) {

        eventRepository.save(event);

    }

    @Override
    public void save(Pay pay) {

        payRepository.save(pay);
    }

    @Override
    public void save(String orderId, List<Long> customerHomePartyIds) {

        ArrayList<PayHomeParty> payHomeParties = new ArrayList<>();
        for (Long customerHomePartyId : customerHomePartyIds) {

            CustomerHomeParty homeParty = customerHomePartyRepository.findByCustomerHomePartyId(customerHomePartyId);
            if(Objects.isNull(homeParty)){
                throw new NotFoundException(ErrorCode.CUSTOMER_HOME_PARTY_NOT_FOUND);
            }

            payHomeParties.add(
                    PayHomeParty
                            .builder()
                            .customerHomeParty(homeParty)
                            .orderId(orderId)
                            .budget(homeParty.getBudget())
                            .build()
            );

            customerHomePartyRepository.updatePartyStatusByCustomerHomePartyId(homeParty.getCustomerHomePartyId(), HomePartyStatus.COMPLETED);

        }

        payHomePartyRepository.saveAll(payHomeParties);

    }

}
