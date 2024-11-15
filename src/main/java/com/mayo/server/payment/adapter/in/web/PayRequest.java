package com.mayo.server.payment.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PayRequest (

        @NotNull(message = "결제 id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "결제 id는 빈 값이 될 수 없습니다.")
        String mid,

        @NotNull(message = "version 정보는 빈 값이 될 수 없습니다.")
        Integer version,

        @NotNull(message = "결제 id 빈 값이 될 수 없습니다.")
        @NotBlank(message = "결제 id 빈 값이 될 수 없습니다.")
        String paymentKey,

        @NotNull(message = "상태는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "상태는 빈 값이 될 수 없습니다.")
        String status,

        @NotNull(message = "주문 id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "주문 id는 빈 값이 될 수 없습니다.")
        String orderId,

        @NotNull(message = "주문 이름 빈 값이 될 수 없습니다.")
        @NotBlank(message = "주문 이름은 빈 값이 될 수 없습니다.")
        String orderName,

        @NotNull(message = "요청일자는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "요청일자는 빈 값이 될 수 없습니다.")
        String requestedAt,

        @NotNull(message = "승인일자는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "승인일자는 빈 값이 될 수 없습니다.")
        String approvedAt,

        @NotNull(message = "전체 금액은 빈 값이 될 수 없습니다.")
        Long totalAmount,

        @NotNull(message = "결제 방법은 빈 값이 될 수 없습니다.")
        @NotBlank(message = "결제 방법은 빈 값이 될 수 없습니다.")
        String method,

        @NotNull(message = "구매자 이름 빈 값이 될 수 없습니다.")
        @NotBlank(message = "구매자 이름은 빈 값이 될 수 없습니다.")
        String customerName,

        @NotNull(message = "Metadata는 빈 값이 될 수 없습니다.")
        PayMetadata metadata
) {
}