package com.mayo.server.customer.domain.repository;

import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import java.util.List;

public interface KitchenRepositoryCustom {

    List<CustomerKitchenListDto> getKitchenAllList(Long userId);
}
