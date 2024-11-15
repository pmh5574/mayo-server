package com.mayo.server.party.app.port.out;

import com.mayo.server.common.utility.DateUtility;

import java.time.LocalDateTime;

public record ChefPartyWaitingDto(

        Long id,

        String info,

        LocalDateTime scheduleAt

) {

    public String getPlainSchedule() {

        return DateUtility.replacePlainTimeFromLocaleDate(this.scheduleAt);
    }
}
