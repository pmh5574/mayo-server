package com.mayo.server.party.app.service;

import com.mayo.server.party.adapter.in.web.AcceptRequest;
import com.mayo.server.party.app.port.in.AcceptInputPort;
import com.mayo.server.party.app.port.in.AcceptUseCase;
import com.mayo.server.party.app.port.in.PartyBoardInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChefAcceptService implements AcceptUseCase {

    private final AcceptInputPort acceptInputPort;

    @Override
    @Transactional
    public void accept(Long chefId, Long customerHomePartyId, AcceptRequest req) {

        if (req.accept()) {
            accepted(chefId, customerHomePartyId, req);
        } else {
            rejected(chefId, customerHomePartyId, req);
        }

    }

    private void accepted(
            Long chefId, Long customerHomePartyId, AcceptRequest req
    ) {

        acceptInputPort.accepted(customerHomePartyId, 1);
    }

    private void rejected(
            Long chefId, Long customerHomePartyId, AcceptRequest req
    ) {

        acceptInputPort.rejected(customerHomePartyId, 0);
    }

}
