package com.mayo.server.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhoneCode {
    KOREA("+82");

    private final String codeNum;
}
