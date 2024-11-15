package com.mayo.server.common.exception;

import com.mayo.server.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest;

@Getter
public abstract class CustomException extends RuntimeException {

    protected String name;
    protected String errorCode;
    protected HttpStatus statusCode;
    protected String message;

}