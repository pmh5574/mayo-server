package com.mayo.server.payment.app.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PayCheckSumMetadata (

        Long amount,

        String paymentKey,

        String methodId

) {
}
