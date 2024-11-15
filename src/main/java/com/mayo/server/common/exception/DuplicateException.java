package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DuplicateException extends CustomException {

    public DuplicateException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = errorCode.name();
        this.statusCode = HttpStatus.BAD_REQUEST;
        this.message = errorCode.getMessage();

    }

}
