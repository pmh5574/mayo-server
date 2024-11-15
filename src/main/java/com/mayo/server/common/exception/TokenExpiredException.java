package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends CustomException {
    public TokenExpiredException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "TokenExpiredException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }
}
