package com.mayo.server.party.app.port.out;

import com.mayo.server.party.domain.model.CustomerHomePartyServices;

import java.util.List;

public record HomePartyDto (

        Long customerHomePartyId,

        String partyInfo,

        String partySchedule,

        String partyCapacity,

        String partyComment,

        List<CustomerHomePartyServices> customerHomePartyServicesList
){

}
