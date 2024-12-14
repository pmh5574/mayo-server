package com.mayo.server.party.adapter.out.persistence;

import com.mayo.server.party.app.port.out.HomePartyFinishListDto;
import java.util.List;

public record HomePartyFinishListResponse(
        List<HomePartyFinishListDto> homePartyFinishList,
        Long count
) {

    public String getCount() {
        return Long.toString(count);
    }
}
