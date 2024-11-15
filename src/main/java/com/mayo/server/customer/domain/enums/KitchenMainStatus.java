package com.mayo.server.customer.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KitchenMainStatus {
    MAIN("메인 주방"),
    NOT_MAIN("메인 주방 X");

    private final String text;

}
