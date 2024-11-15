package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import com.mayo.server.customer.app.port.in.CustomerS3InputPort;
import com.mayo.server.customer.app.port.in.CustomerTransformedImage;
import com.mayo.server.customer.app.port.in.KitchenQueryInputPort;
import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.mayo.server.customer.domain.model.KitchenTools;
import com.mayo.server.customer.domain.repository.KitchenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class KitchenQueryAdapter implements KitchenQueryInputPort {

    private final KitchenRepository kitchenRepository;

    private final CustomerS3InputPort customerS3InputPort;

    @Override
    public Kitchen postKitchen(final KitchenRegister kitchenRegister) {
        return kitchenRepository.save(Kitchen.builder()
                .mainStatus(KitchenMainStatus.NOT_MAIN)
                .nickName(kitchenRegister.nickName())
                .address(kitchenRegister.address())
                .addressSub(kitchenRegister.addressSub())
                .burnerType(kitchenRegister.burnerType())
                .burnerQuantity(kitchenRegister.burnerQuantity())
                .additionalEquipment(kitchenRegister.additionalEquipment())
                .requirements(kitchenRegister.requirements())
                .considerations(kitchenRegister.considerations())
                .build());
    }

    @Override
    public List<KitchenImages> postKitchenImages(final List<KitchenImagesRegister> kitchenImagesRegisters, final Long userId) {

        List<CustomerTransformedImage> customerTransformedImages = customerS3InputPort.postKitchenImages(
                kitchenImagesRegisters, userId);

        return customerTransformedImages.stream()
                .map(customerTransformedImage ->
                        KitchenImages.builder()
                        .order(customerTransformedImage.id())
                        .imageName(customerTransformedImage.url())
                                .build())
                .toList();
    }

    @Override
    public List<KitchenTools> postKitchenTools(final List<KitchenToolsRegister> kitchenToolsRegisters) {
        return kitchenToolsRegisters.stream()
                .map(kitchenToolsRegister -> new KitchenTools(kitchenToolsRegister.toolName()))
                .toList();
    }

    @Override
    public Kitchen findByKitchen(final Long kitchenId) {
        return kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.KITCHEN_NOT_FOUND));
    }

    @Override
    public List<KitchenImages> editKitchenImages(final List<KitchenImagesRegister> kitchenImagesRegisters,
                                                 final Long userId) {
        customerS3InputPort.deleteS3Images(userId);

        List<CustomerTransformedImage> customerTransformedImages = customerS3InputPort.postKitchenImages(
                kitchenImagesRegisters, userId);

        return customerTransformedImages.stream()
                .map(customerTransformedImage ->
                        KitchenImages.builder()
                                .order(customerTransformedImage.id())
                                .imageName(customerTransformedImage.url())
                                .build())
                .toList();
    }

    @Override
    public List<KitchenTools> editKitchenTools(final List<KitchenToolsRegister> kitchenToolsRegisters) {
        return kitchenToolsRegisters.stream()
                .map(kitchenToolsRegister -> new KitchenTools(kitchenToolsRegister.toolName()))
                .toList();
    }

    @Override
    public Kitchen findByKitchenAndCustomerId(final Long kitchenId, final Long customerId) {
        return kitchenRepository.findByKitchenIdAndCustomerId(kitchenId, customerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.KITCHEN_NOT_FOUND));
    }

    @Override
    public List<Kitchen> findKitchenListByCustomerId(final Long customerId) {
        return kitchenRepository.findByCustomerId(customerId);
    }

    @Override
    public boolean findByCustomerIdAndMainStatus(final Long userId,
                                                           final KitchenMainStatus kitchenMainStatus) {
        return kitchenRepository.findByCustomerIdAndMainStatus(userId, kitchenMainStatus)
                .isPresent();
    }

    @Override
    public List<CustomerKitchenListDto> getKitchenAllList(final Long userId) {
        return kitchenRepository.getKitchenAllList(userId);
    }

    @Override
    public Kitchen getMainKitchen(final Long userId, final KitchenMainStatus status) {
        return kitchenRepository.findByCustomerIdAndMainStatus(userId, status)
                .orElse(null);
    }
}
