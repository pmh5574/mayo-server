package com.mayo.server.party.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewFoodReason {

    SPECIAL_MENU("특별한 메뉴가 있어요"),
    TASTY_FOOD("음식이 맛있어요"),
    UNIQUE_COURSE("코스가 독특해요"),
    LARGE_PORTIONS("양이 많아요"),
    WELL_STRUCTURED_MENU("메뉴 구성이 알차요"),
    TIMELY_DISH("요리가 시간에 맞게 나와요"),
    WORTH_THE_PRICE("비싼 만큼 가치있어요"),
    PERFECT_FOR_SPECIAL_OCCASIONS("특별한 날에 딱이에요"),
    ARTISTIC_PLATING("플레이팅이 예술이에요");

    private final String text;
}
