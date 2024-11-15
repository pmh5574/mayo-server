package com.mayo.server.customer.adapter.out.persistence;

import java.util.List;
import lombok.Builder;

public record CustomerMainKitchenResponse(
        Long id,

        String nickName,

        String address,

        String addressSub,

        String burnerType,

        String burnerQuantity,

        List<String> kitchenImagesRegisterList,

        List<String> kitchenToolsRegisterList,

        String additionalEquipment,

        String requirements,

        String considerations
) {
    public String getId() {
        return id.toString();
    }

    @Builder
    public CustomerMainKitchenResponse(final Long id, final String nickName, final String address, final String addressSub,
                                       final String burnerType,
                                       final String burnerQuantity, final List<String> kitchenImagesRegisterList,
                                       final List<String> kitchenToolsRegisterList, final String additionalEquipment,
                                       final String requirements,
                                       final String considerations) {
        this.id = id;
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
