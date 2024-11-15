package com.mayo.server.payment.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PayMetadata (

        @NotNull(message = "고객 Key는 빈값이 될 수 없습니다.")
        @NotBlank(message = "고객 Key는 빈값이 될 수 없습니다.")
        String customerKey,

        @NotNull(message = "고객 id는 빈값이 될 수 없습니다.")
        Long customerId,

        @NotNull(message = "홈파티 id 리스트는 빈값이 될 수 없습니다.")
        @NotEmpty(message = "홈파티 id 리스트는 빈값이 될 수 없습니다.")
        List<Long> customerHomePartyIds
) {
}