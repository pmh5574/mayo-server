package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmailVerifyRequest {

    public record UsernameRequest (

            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email,

            @NotNull(message = "name is not null")
            @NotBlank(message = "name is not empty")
            String name,

            @NotNull(message = "authNum is not null")
            @NotBlank(message = "authNum is not empty")
            String authNum
    ){

    }

    public record PwdRequest (

            @NotNull(message = "username is not null")
            @NotBlank(message = "username is not empty")
            String username,
            @NotNull(message = "email is not null")
            @NotBlank(message = "email is not empty")
            String email,

            @NotNull(message = "name is not null")
            @NotBlank(message = "name is not empty")
            String name,

            @NotNull(message = "birthday is not null")
            @NotBlank(message = "birthday is not empty")
            String birthday,

            @NotNull(message = "authNum is not null")
            @NotBlank(message = "authNum is not empty")
            String authNum
    ){

    }
}
