package com.mayo.server.customer.adapter.out.persistence;

import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import java.util.List;
import lombok.Builder;

public record CustomerKitchenResponse(
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
    @Builder
    public CustomerKitchenResponse(final String nickName, final String address, final String addressSub,
                                   final String burnerType,
                                   final String burnerQuantity,
                                   final List<KitchenImagesRegister> kitchenImagesRegisterList,
                                   final List<KitchenToolsRegister> kitchenToolsRegisterList,
                                   final String additionalEquipment,
                                   final String requirements, final String considerations) {
        this.nickName = nickName;
        this.address = address;
        this.addressSub = addressSub;
        this.burnerType = burnerType;
        this.burnerQuantity = burnerQuantity;
        this.kitchenImagesRegisterList = kitchenImagesRegisterList;
        this.kitchenToolsRegisterList = kitchenToolsRegisterList;
        this.additionalEquipment = additionalEquipment;
        this.requirements = requirements;
        this.considerations = considerations;
    }
}
