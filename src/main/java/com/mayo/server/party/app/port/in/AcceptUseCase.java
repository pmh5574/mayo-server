package com.mayo.server.party.app.port.in;

import com.mayo.server.party.adapter.in.web.AcceptRequest;

public interface AcceptUseCase {

    void accept(Long chefId, Long customerHomePartyId, AcceptRequest req);
}
