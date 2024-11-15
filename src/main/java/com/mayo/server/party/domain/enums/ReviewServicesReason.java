package com.mayo.server.party.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewServicesReason {

    HYGIENIC("위생적이에요"),
    FRIENDLY("친절해요"),
    MINIMAL_CONVERSATION("필요한 대화만 오가요"),
    GOOD_PERFORMANCE("퍼포먼스가 좋아요"),
    COOKS_WELL("원하는 음식을 잘해줬어요"),
    ENJOY_CONVERSATION("대화가 즐거워요"),
    COMFORTABLE_ATMOSPHERE("분위기가 편안해요"),
    EXPLAINS_WELL("설명을 잘해주세요"),
    FAST_SERVICE("음식이 빨리 나와요"),
    NO_EXCESSIVE_RECOMMENDATION("과도한 권유가 없어요");

    private final String text;
}
