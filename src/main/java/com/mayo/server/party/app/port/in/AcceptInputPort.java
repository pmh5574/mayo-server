package com.mayo.server.party.app.port.in;

public interface AcceptInputPort {

    void accepted(Long customerHomePartyId, Integer matched);

    void rejected(Long customerHomePartyId, Integer matched);
}
