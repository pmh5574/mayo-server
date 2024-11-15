package com.mayo.server.customer.adapter.out.persistence;

import lombok.Builder;

public record CustomerMyPageProfile(
        String id,
        String username,
        String name,
        String birthDay,
        String phone,
        String email
) {

    @Builder
    public CustomerMyPageProfile(final String id, final String username, final String name, final String birthDay, final String phone,
                                 final String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthDay = birthDay;
        this.phone = phone;
        this.email = email;
    }
}
