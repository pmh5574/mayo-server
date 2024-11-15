package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest (

        @NotBlank(message = "username is not empty")
        @NotNull(message = "username is not null")
        String username,

        @NotBlank(message = "password is not empty")
        @NotNull(message = "password is not null")
        String password
) {
}
