package com.mayo.server.party.adapter.out.persistence;

import com.mayo.server.party.app.port.out.HomePartyStatusListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import java.util.List;

public record HomePartyStatusResponse(
        HomePartyStatus status,
        List<HomePartyStatusListDto> list
) {
}
