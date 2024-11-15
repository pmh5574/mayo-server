package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.mayo.server.customer.domain.model.KitchenTools;
import java.util.List;

public interface KitchenQueryInputPort {

    Kitchen postKitchen(KitchenRegister kitchenRegister);

    List<KitchenImages> postKitchenImages(List<KitchenImagesRegister> kitchenImagesRegisters,
                                    Long userId);

    List<KitchenTools> postKitchenTools(List<KitchenToolsRegister> kitchenToolsRegisters);

    Kitchen findByKitchen(Long kitchenId);

    List<KitchenImages> editKitchenImages(List<KitchenImagesRegister> kitchenImagesRegisters, Long userId);

    List<KitchenTools> editKitchenTools(List<KitchenToolsRegister> kitchenToolsRegisters);

    Kitchen findByKitchenAndCustomerId(Long kitchenId, Long customerId);

    List<Kitchen> findKitchenListByCustomerId(Long customerId);

    boolean findByCustomerIdAndMainStatus(Long userId, KitchenMainStatus kitchenMainStatus);

    List<CustomerKitchenListDto> getKitchenAllList(Long userId);

    Kitchen getMainKitchen(Long userId, KitchenMainStatus status);
}
