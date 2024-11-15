package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends CustomException {

    public InvalidRequestException(ErrorCode errorCode, String message) {

        this.errorCode = errorCode.getCode();
        this.name = "InvalidRequestException";
        this.message = message;
        this.statusCode = HttpStatus.BAD_REQUEST;

    }

    public InvalidRequestException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "InvalidRequestException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;

    }

}
