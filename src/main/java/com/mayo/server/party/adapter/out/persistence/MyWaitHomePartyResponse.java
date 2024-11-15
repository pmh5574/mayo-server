package com.mayo.server.party.adapter.out.persistence;

import java.math.BigDecimal;

public record MyWaitHomePartyResponse(

        String info,

        String address,

        String scheduleAt,

        String capacity,

        BigDecimal budget,

        String createdAt
) {
}
