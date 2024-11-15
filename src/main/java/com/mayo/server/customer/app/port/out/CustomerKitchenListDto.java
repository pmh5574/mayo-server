package com.mayo.server.customer.app.port.out;

import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import java.util.List;

public record CustomerKitchenListDto(
        Long id,

        KitchenMainStatus kitchenMainStatus,

        String nickName,

        String address,

        String addressSub,

        List<String> imageNames
) {
}
