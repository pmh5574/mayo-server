package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotGenerateException extends CustomException {

    public NotGenerateException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "NotGenerateException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

}
