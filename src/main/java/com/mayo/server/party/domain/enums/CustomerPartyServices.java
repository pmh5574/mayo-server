package com.mayo.server.party.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerPartyServices {
    COURSE_PLANNING("코스 구성"),
    INGREDIENT_SELECTION("재료 선정"),
    INGREDIENT_PURCHASE("재료 구입"),
    CLEANUP("뒷정리");

    private final String text;
}
