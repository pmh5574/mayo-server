package com.mayo.server.common;

import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.CustomException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponse {

    private Integer statusCode;
    private String name;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(CustomException e){

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponse.builder()
                        .statusCode(e.getStatusCode().value())
                        .name(e.getName())
                        .code(e.getErrorCode())
                        .message(e.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(Exception e){
        return ResponseEntity
                .status(500)
                .body(ErrorResponse.builder()
                        .statusCode(500)
                        .name(e.getClass().getName())
                        .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                        .message(e.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatusCode statusCode, CustomException exception){
        return ResponseEntity
                .status(statusCode)
                .body(ErrorResponse.builder()
                        .statusCode(statusCode.value())
                        .name(exception.getClass().getName())
                        .code(exception.getErrorCode())
                        .message(exception.getMessage())
                        .build());
    }

}
