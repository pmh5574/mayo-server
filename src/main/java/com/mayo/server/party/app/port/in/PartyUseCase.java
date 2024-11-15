package com.mayo.server.party.app.port.in;

public interface PartyUseCase {

    void apply(Long id, Long chefId);
}
