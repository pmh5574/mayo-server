package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

@Getter
public class NotFoundException extends CustomException implements Supplier<CustomException>  {

    public NotFoundException(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
        this.name = "NotFoundException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

    @Override
    public CustomException get() {
        return null;
    }
}
