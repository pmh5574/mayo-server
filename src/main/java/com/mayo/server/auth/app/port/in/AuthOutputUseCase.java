package com.mayo.server.auth.app.port.in;

import com.mayo.server.auth.adapter.out.persistence.CustomerInfoResponse;
import com.mayo.server.chef.domain.model.Chef;

public interface AuthOutputUseCase {

    Chef getChefInfo(Long id);

    CustomerInfoResponse getCustomerInfo(String token);
}
