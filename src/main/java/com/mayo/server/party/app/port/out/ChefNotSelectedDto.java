package com.mayo.server.party.app.port.out;

import java.util.List;

public record ChefNotSelectedDto(
        Long id,
        String chefName,
        String chefInfoExperience,
        String chefInfoAdditional,
        Long partyScheduleId,
        List<ChefNotSelectedHashTagDto> chefHashList
) {

    public String getId() {
        return String.valueOf(id);
    }

    public String getPartyScheduleId() {
        return String.valueOf(partyScheduleId);
    }
}
