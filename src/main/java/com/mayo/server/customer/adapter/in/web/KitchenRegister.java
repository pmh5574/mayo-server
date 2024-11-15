package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record KitchenRegister(

        @NotNull(message = "주방 닉네임을 입력해 주세요.")
        @NotBlank(message = "주방 닉네임을 입력해 주세요.")
        String nickName,

        @NotNull(message = "주소를 입력해 주세요.")
        @NotBlank(message = "주소를 입력해 주세요.")
        String address,

        @NotNull(message = "상세 주소를 입력해 주세요.")
        @NotBlank(message = "상세 주소를 입력해 주세요.")
        String addressSub,

        @NotNull(message = "화구 종류를 선택해 주세요.")
        @NotBlank(message = "화구 종류를 선택해 주세요.")
        String burnerType,

        @NotNull(message = "화구 개수를 입력해 주세요.")
        @NotBlank(message = "화구 개수를 입력해 주세요.")
        String burnerQuantity,

        List<KitchenImagesRegister> kitchenImagesRegisterList,

        @NotEmpty(message = "조리 기구 및 도구를 선택해 주세요.")
        List<KitchenToolsRegister> kitchenToolsRegisterList,

        String additionalEquipment,

        String requirements,

        String considerations
) {
}
