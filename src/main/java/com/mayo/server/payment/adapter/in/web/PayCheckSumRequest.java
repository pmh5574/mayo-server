package com.mayo.server.payment.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PayCheckSumRequest (

        @NotNull(message = "주문 id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "주문 id는 빈 값이 될 수 없습니다.")
        String orderId,

        @NotNull(message = "결제 금액은 빈 값이 될 수 없습니다.")
        Long amount,

        @NotNull(message = "결제 id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "결제 id는 빈 값이 될 수 없습니다.")
        String paymentKey,

        @NotNull(message = "결제 method Id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "결제 method Id 빈 값이 될 수 없습니다.")
        String methodId
) {
}