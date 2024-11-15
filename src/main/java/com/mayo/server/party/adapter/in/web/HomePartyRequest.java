package com.mayo.server.party.adapter.in.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HomePartyRequest(

        @NotNull(message = "빈 값은 허용 되지 않습니다.")
        @NotEmpty(message = "빈 값은 허용 되지 않습니다.")
        List<String> days
) {
}
