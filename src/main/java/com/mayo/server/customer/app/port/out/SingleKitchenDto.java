package com.mayo.server.customer.app.port.out;

import java.time.LocalDateTime;

public record SingleKitchenDto (

        Long kitchenId,

        String nickName,

        String address,

        String addressSub,

        String burnerType,

        String burnerQuantity,

        String additionalEquipment,

        String requirements,

        String considerations,

        LocalDateTime deletedAt
) {
}
