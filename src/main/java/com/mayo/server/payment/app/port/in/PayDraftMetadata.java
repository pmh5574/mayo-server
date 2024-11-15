package com.mayo.server.payment.app.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PayDraftMetadata(

        String customerKey,

        Long customerId,

        List<Long> customerHomePartyIds,

        String orderName,

        Long amount,

        String customerName
) {
}
