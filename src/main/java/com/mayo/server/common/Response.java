package com.mayo.server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Response<T> {

    private HttpStatus status;

    private T result;

    public Response() {

        this.status = HttpStatus.OK;
        this.result = (T) "Success";
    }
    public Response(T result) {
        this.status = HttpStatus.OK;
        this.result = result;
    }

}
