package com.mayo.server.chef.adapter.out.persistence;

public class VerifyResponse {

    public record UsernameResponse (

            String username
    ) {

    }

    public record PwdResponse (

            String password
    ) {

    }
}
