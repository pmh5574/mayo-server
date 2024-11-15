package com.mayo.server.auth.adapter.out.persistence;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.enums.UserType;

public record ChefInfoResponse (

        Long id,

        String role
) {

    public static ChefInfoResponse getChefInfo(Chef chef) {

        return new ChefInfoResponse(
                chef.getId(),
                UserType.CHEF.name()
        );
    }
}
