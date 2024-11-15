package com.mayo.server.party.adapter.in.web;

import com.mayo.server.party.domain.enums.CustomerPartyServices;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record CustomerPartyRegisterRequest(

        @Positive(message = "유효한 주방 ID를 입력해 주세요.")
        @NotNull(message = "주방을 선택해 주세요.")
        Long kitchenId,

        @NotNull(message = "홈파티 한 줄 소개를 입력해주세요.")
        @NotBlank(message = "홈파티 한 줄 소개를 입력해주세요.")
        String partyInfo,

        @NotNull(message = "홈파티 일시를 입력해주세요.")
        @NotBlank(message = "홈파티 일시를 입력해주세요.")
        @Pattern(
                regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])([01]\\d|2[0-3])([0-5]\\d)([0-5]\\d)$",
                message = "홈파티 일시는 YYYYMMDDHHMMSS 형식이어야 하며, 유효한 날짜여야 합니다."
        )
        String partySchedule,

        @NotNull(message = "홈파티 인원 수를 입력해주세요.")
        @Min(value = 0, message = "어린이 인원 수는 0 이상이어야 합니다.")
        Integer adultCount,

        @NotNull(message = "홈파티 인원 수를 입력해주세요.")
        @Min(value = 0, message = "어린이 인원 수는 0 이상이어야 합니다.")
        Integer childCount,

        @Digits(integer = 15, fraction = 0, message = "가격은 최대 15자리 숫자여야 합니다.")
        @NotNull(message = "예산을 입력해주세요.")
        BigDecimal budget,

        @NotNull(message = "홈파티 요청 서비스를 선택해주세요.")
        List<CustomerPartyServices> partyServices,

        String partyComment
) {
}
