package com.mayo.server.party.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerPartyFinishSearchRequest(

        @NotNull(message = "이용 기간 설정을 해주세요.")
        @NotBlank(message = "이용 기간 설정을 해주세요.")
        @Pattern(
                regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                message = "홈파티 일시는 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
        )
        String startDate,

        @NotNull(message = "이용 기간 설정을 해주세요.")
        @NotBlank(message = "이용 기간 설정을 해주세요.")
        @Pattern(
                regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                message = "홈파티 일시는 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
        )
        String endDate,

        @NotNull(message = "잘못된 요청입니다.")
        Integer page
) {
}