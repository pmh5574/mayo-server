package com.mayo.server.payment.domain.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayTypeCode {

    DRAFT("DRAFT"),
    REQUEST("REQUEST"),
    CHECK_SUM("CHECK_SUM"),
    APPROVE("APPROVE"),
    COMPLETE("COMPLETE"),
    REJECT("REJECT");

    private final String code;
}