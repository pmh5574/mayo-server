package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmailRequest {

    public record RegisterRequest (

            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email

    ) {}

    public record UsernameRequest (

            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email

    ) {}

    public record PwdRequest (

            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email

    ) {}

    public record MyPageRequest (

            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email

    ) {}

}
