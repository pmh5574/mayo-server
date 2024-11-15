package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class AuthException extends CustomException implements Supplier<CustomException> {

    public AuthException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "AuthException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.UNAUTHORIZED;

    }

    @Override
    public CustomException get() {
        return null;
    }
}
