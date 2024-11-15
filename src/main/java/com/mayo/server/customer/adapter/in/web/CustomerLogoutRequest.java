package com.mayo.server.customer.adapter.in.web;

import jakarta.validation.constraints.NotNull;

public record CustomerLogoutRequest(
        @NotNull(message = "id 값이 누락되었습니다.")
        Long id) {
}
