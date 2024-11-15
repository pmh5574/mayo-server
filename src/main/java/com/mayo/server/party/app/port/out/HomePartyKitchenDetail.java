package com.mayo.server.party.app.port.out;

import java.util.Set;

public record HomePartyKitchenDetail(
        String nickName,
        String address,
        String addressSub,
        Set<String> kitchenImagesList
) {
}
