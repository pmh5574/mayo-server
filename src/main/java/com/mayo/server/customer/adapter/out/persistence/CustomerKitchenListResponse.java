package com.mayo.server.customer.adapter.out.persistence;

import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import java.util.List;

public record CustomerKitchenListResponse(
        Long id,

        KitchenMainStatus kitchenMainStatus,

        String nickName,

        String address,

        String addressSub,

        List<String> imageName

) {
    public String getId() {
        return id.toString();
    }
}
