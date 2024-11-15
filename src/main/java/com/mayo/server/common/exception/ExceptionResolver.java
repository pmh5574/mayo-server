package com.mayo.server.common.exception;

import com.mayo.server.common.ErrorResponse;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.utility.LoggerUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionResolver {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(
            {
                    Exception.class,
                    RuntimeException.class,
                    IllegalArgumentException.class
            }
    )
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(
            HttpServletRequest req,
            Exception e
    ) {

        InternalServerException exception = new InternalServerException(
                ErrorCode.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );

        return ErrorResponse.toResponseEntity(exception);
    }

    @ExceptionHandler(
            {
                    CustomException.class
            }
    )
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleCustomException(
            HttpServletRequest req,
            Exception e
    ) {

        if (e instanceof CustomException) {
            return ErrorResponse.toResponseEntity(((CustomException) e).getStatusCode(), (CustomException) e);
        } else {
            return ErrorResponse.toResponseEntity(e);
        }

    }

    @ExceptionHandler({AwsSystemException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAwsException(HttpServletRequest req, CustomException e) {

        return ErrorResponse.toResponseEntity(e);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleRequestException(HttpServletRequest req, MethodArgumentNotValidException e) {

        InvalidRequestException exception = new InvalidRequestException(
                ErrorCode.CHEF_NOT_FOUND,
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage()
        );

        return ErrorResponse.toResponseEntity(exception);
    }


}