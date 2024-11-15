package com.mayo.server.customer.adapter.in.web;

import java.util.List;

public record KitchenEdit(

        String nickName,

        String address,

        String addressSub,

        String burnerType,

        String burnerQuantity,

        List<KitchenImagesRegister> kitchenImagesRegisterList,

        List<KitchenToolsRegister> kitchenToolsRegisterList,

        String additionalEquipment,

        String requirements,

        String considerations
) {
}
