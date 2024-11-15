package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AwsSystemException extends CustomException {

    public AwsSystemException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "AwsSystemException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
