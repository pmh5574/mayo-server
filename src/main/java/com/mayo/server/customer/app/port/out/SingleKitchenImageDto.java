package com.mayo.server.customer.app.port.out;

import java.time.LocalDateTime;

public record SingleKitchenImageDto(

        Long kitchenImagesId,

        String imageName,

        Integer order,

        LocalDateTime deletedAt

) {
}
