package com.mayo.server.party.app.port.out;

import java.util.Set;

public record HomePartyChefDetail(
        String chefName,
        String chefInfoExperience,
        String chefInfoIntroduce,
        String chefInfoRegion,
        String chefInfoAdditional,
        String chefInfoDescription,
        Set<String> chefPortfolioImage
) {
}
