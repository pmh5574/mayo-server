package com.mayo.server.auth.app.service;

import com.mayo.server.auth.adapter.out.persistence.CustomerInfoResponse;
import com.mayo.server.auth.app.port.in.AuthOutputUseCase;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthOutputUseCase {

    private final ChefQueryOutputPort chefQueryOutputPort;
    private final JwtService jwtService;

    @Override
    public Chef getChefInfo(Long id) {
        return chefQueryOutputPort.getChefById(String.valueOf(id));
    }

    @Override
    public CustomerInfoResponse getCustomerInfo(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        return new CustomerInfoResponse(String.valueOf(userId), UserType.CUSTOMER.name());
    }

}
