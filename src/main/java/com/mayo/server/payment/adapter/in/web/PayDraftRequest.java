package com.mayo.server.payment.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PayDraftRequest (

        @NotNull(message = "주문 id는 빈 값이 될 수 없습니다.")
        @NotBlank(message = "주문 id는 빈 값이 될 수 없습니다.")
        String orderId,

        @NotNull(message = "주문 이름은 빈 값이 될 수 없습니다.")
        @NotBlank(message = "주문 이름은 빈 값이 될 수 없습니다.")
        String orderName,

        @NotNull(message = "결제 금액은 빈 값이 될 수 없습니다.")
        Long amount,

        @NotNull(message = "고객의 이름은 빈 값이 될 수 없습니다.")
        @NotNull(message = "고객의 이름은 빈 값이 될 수 없습니다.")
        String customerName,

        @NotNull(message = "추가 정보는 빈 값이 될 수 없습니다.")
        PayMetadata metadata

) {

}