package com.mayo.server.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailTemplate {
    FROM("mayoseoul@gmail.com"),
    REGISTER_SUBJECT("Mayo 입니다. 이메일 인증번호를 확인해 주세요.");

    private final String text;
}
