package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends CustomException {

    public InternalServerException(ErrorCode errorCode, String message) {

        this.message = message;
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = errorCode.getCode();
        this.name = "InternalServerException";
    }

}
