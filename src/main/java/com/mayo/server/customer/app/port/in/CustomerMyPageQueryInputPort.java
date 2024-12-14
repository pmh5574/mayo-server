package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenTools;
import java.util.List;

public interface CustomerMyPageQueryInputPort {
    Customer getCustomerProfile(Long customerId);

    Kitchen postKitchen(KitchenRegister kitchenRegister, Long userId);

    List<CustomerTransformedSaveImage> postImages(List<KitchenImagesRegister> kitchenImagesRegisters, Long userId);

    List<KitchenTools> postTools(List<KitchenToolsRegister> kitchenToolsRegisters);

    Kitchen findByKitchen(Long kitchenId);

    List<CustomerTransformedSaveImage> editImages(List<KitchenImagesRegister> kitchenImagesRegisters, Long userId);

    List<KitchenTools> editTools(List<KitchenToolsRegister> kitchenToolsRegisters);

    void checkPhoneAndAuthCode(String phone, String authNum, CustomerVerificationStatus edit);

    void checkEmailAndAuthCode(String email, String authNum, CustomerVerificationStatus edit);

    void deleteCustomerPhone(String phone);

    void deleteCustomerEmail(String email);

    List<Kitchen> getKitchenList(Long userId);

    boolean hasMainKitchen(KitchenMainStatus kitchenMainStatus, Long userId);

    List<CustomerKitchenListDto> getKitchenAllList(Long userId);

    Kitchen getMainKitchen(Long userId, KitchenMainStatus status);
}
