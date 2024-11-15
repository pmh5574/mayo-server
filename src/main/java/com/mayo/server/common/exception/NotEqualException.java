package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotEqualException extends CustomException {

    public NotEqualException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "NotEqualException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

}
