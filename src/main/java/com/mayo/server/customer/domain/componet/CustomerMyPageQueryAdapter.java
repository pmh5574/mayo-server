package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import com.mayo.server.customer.app.port.in.CustomerEmailQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerMyPageQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerPhoneQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerQueryInputPort;
import com.mayo.server.customer.app.port.in.KitchenQueryInputPort;
import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.mayo.server.customer.domain.model.KitchenTools;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerMyPageQueryAdapter implements CustomerMyPageQueryInputPort {

    private final CustomerQueryInputPort customerQueryInputPort;
    private final KitchenQueryInputPort kitchenQueryInputPort;
    private final CustomerEmailQueryInputPort customerEmailQueryInputPort;
    private final CustomerPhoneQueryInputPort customerPhoneQueryInputPort;

    @Override
    public Customer getCustomerProfile(final Long customerId) {
        return customerQueryInputPort.findById(customerId);
    }

    @Override
    public Kitchen postKitchen(final KitchenRegister kitchenRegister, final Long userId) {
        Kitchen kitchen = kitchenQueryInputPort.postKitchen(kitchenRegister);

        Customer customer = customerQueryInputPort.findById(userId);
        customer.addKitchen(kitchen);

        return kitchen;
    }

    @Override
    public List<KitchenImages> postImages(final List<KitchenImagesRegister> kitchenImagesRegisters,
                                    final Long userId) {
        return kitchenQueryInputPort.postKitchenImages(kitchenImagesRegisters, userId);
    }

    @Override
    public List<KitchenTools> postTools(final List<KitchenToolsRegister> kitchenToolsRegisters) {
        return kitchenQueryInputPort.postKitchenTools(kitchenToolsRegisters);
    }

    @Override
    public Kitchen findByKitchen(final Long kitchenId) {
        return kitchenQueryInputPort.findByKitchen(kitchenId);
    }

    @Override
    public List<KitchenImages> editImages(final List<KitchenImagesRegister> kitchenImagesRegisters, final Long userId) {
        return kitchenQueryInputPort.editKitchenImages(kitchenImagesRegisters, userId);
    }

    @Override
    public List<KitchenTools> editTools(final List<KitchenToolsRegister> kitchenToolsRegisters) {
        return kitchenQueryInputPort.editKitchenTools(kitchenToolsRegisters);
    }

    @Override
    public void checkPhoneAndAuthCode(final String phone, final String authNum, final CustomerVerificationStatus status) {
        customerPhoneQueryInputPort.checkFindIdByPhoneAndAuthCodeAndStatus(phone, authNum, status);
    }

    @Override
    public void checkEmailAndAuthCode(final String email, final String authNum, final CustomerVerificationStatus edit) {
        customerEmailQueryInputPort.checkFindIdByEmailAndAuthCodeAndStatus(email, authNum, edit);
    }

    @Override
    public void deleteCustomerPhone(final String phone) {
        customerPhoneQueryInputPort.deleteCustomerPhone(phone);
    }

    @Override
    public void deleteCustomerEmail(final String email) {
        customerEmailQueryInputPort.deleteCustomerEmail(email);
    }

    @Override
    public List<Kitchen> getKitchenList(final Long userId) {
        return kitchenQueryInputPort.findKitchenListByCustomerId(userId);
    }

    @Override
    public boolean hasMainKitchen(final KitchenMainStatus kitchenMainStatus, final Long userId) {
        return kitchenQueryInputPort.findByCustomerIdAndMainStatus(userId, kitchenMainStatus);
    }

    @Override
    public List<CustomerKitchenListDto> getKitchenAllList(final Long userId) {
        return kitchenQueryInputPort.getKitchenAllList(userId);
    }

    @Override
    public Kitchen getMainKitchen(final Long userId, final KitchenMainStatus status) {
        kitchenQueryInputPort.getMainKitchen(userId, status);
        return null;
    }
}
