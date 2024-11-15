package com.mayo.server.party.app.port.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MyHomePartyDto(

        String info,

        String address,

        LocalDateTime scheduleAt,

        Integer adultCount,

        Integer childCount,

        BigDecimal budget,

        LocalDateTime createdAt
) {
}
