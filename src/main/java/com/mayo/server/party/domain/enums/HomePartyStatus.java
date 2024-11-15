package com.mayo.server.party.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HomePartyStatus {

    WAITING("셰프 수락 대기"),
    CHEF_NOT_SELECTED("셰프 요청 대기"),
    CHEF_ACCEPTED("셰프 요청 수락(결제 대기)"),
    ACCEPTED("요청 완료(결제 대기)"),
    REJECTED("거절"),
    COMPLETED("예약확정(결제 완료)"),
    FINISH("이용 완료");

    private final String text;
}
