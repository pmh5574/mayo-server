package com.mayo.server.party.app.port.out;

import java.util.List;

public record ChefSearch(
        Long id,
        String chefName,
        String chefInfoExperience,
        String chefInfoAdditional,
        Long hopePay,
        List<ChefSearchHashTag> chefHashList
) {

    public String getId() {
        return String.valueOf(id);
    }

    public String getHopePay() {
        return String.valueOf(hopePay);
    }
}
