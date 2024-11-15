package com.mayo.server.chef.adapter.out.persistence;

public record FindIdResponse (

        String username,
        String createdAt,
        String role
) {
}
