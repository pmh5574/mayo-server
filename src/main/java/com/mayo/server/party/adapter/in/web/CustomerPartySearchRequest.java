package com.mayo.server.party.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerPartySearchRequest(
        @NotNull(message = "검색어를 입력해주세요.")
        @NotBlank(message = "검색어를 입력해주세요.")
        String keyword
) {
}
