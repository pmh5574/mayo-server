package com.mayo.server.customer.app.service;

import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.InvalidRequestException;
import com.mayo.server.common.exception.NotEqualException;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.customer.adapter.in.web.CustomerEdit;
import com.mayo.server.customer.adapter.in.web.KitchenEdit;
import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.in.web.KitchenToolsRegister;
import com.mayo.server.customer.adapter.out.persistence.CustomerImageListResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerKitchenListResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerKitchenResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerMainKitchenResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerMyPageProfile;
import com.mayo.server.customer.app.port.in.CustomerMyPageQueryInputPort;
import com.mayo.server.customer.app.port.in.CustomerTransformedImage;
import com.mayo.server.customer.app.port.in.CustomerTransformedSaveImage;
import com.mayo.server.customer.app.port.out.CustomerKitchenListDto;
import com.mayo.server.customer.domain.enums.CustomerVerificationStatus;
import com.mayo.server.customer.domain.enums.KitchenMainStatus;
import com.mayo.server.customer.domain.model.Customer;
import com.mayo.server.customer.domain.model.Kitchen;
import com.mayo.server.customer.domain.model.KitchenImages;
import com.mayo.server.customer.domain.model.KitchenTools;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerMyPageService {

    private final CustomerMyPageQueryInputPort customerMyPageQueryInputPort;

    private final JwtService jwtService;

    public CustomerMyPageProfile getCustomerProfile(final Long customerId, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Customer customer = customerMyPageQueryInputPort.getCustomerProfile(customerId);

        checkKitchenCustomerAndJwtCustomer(userId, customer.getId());

        return CustomerMyPageProfile.builder()
                .id(String.valueOf(customer.getId()))
                .username(customer.getCustomerUsername())
                .name(customer.getCustomerName())
                .birthDay(customer.getCustomerBirthday())
                .phone(customer.getCustomerPhone())
                .email(customer.getCustomerEmail())
                .build();
    }

    @Transactional
    public CustomerImageListResponse postKitchen(final KitchenRegister kitchenRegister, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Kitchen kitchen = customerMyPageQueryInputPort.postKitchen(kitchenRegister, userId);
        setAsMainKitchenIfNoneExists(kitchen, userId);

        List<CustomerTransformedSaveImage> customerTransformedSaveImages = checkAndAddImages(kitchenRegister, userId);
        List<KitchenImages> kitchenImagesList = changeImages(customerTransformedSaveImages);
        addImages(kitchenImagesList, kitchen);
        checkAndAddTools(kitchenRegister, kitchen);

        List<CustomerTransformedImage> customerTransformedImageList = getCustomerTransformedImages(customerTransformedSaveImages);

        return new CustomerImageListResponse(customerTransformedImageList);
    }

    private void setAsMainKitchenIfNoneExists(final Kitchen kitchen, final Long userId) {
        boolean hasMainKitchen = customerMyPageQueryInputPort.hasMainKitchen(KitchenMainStatus.MAIN, userId);
        if (!hasMainKitchen) {
            kitchen.patchMain(KitchenMainStatus.MAIN);
        }
    }

    private List<CustomerTransformedImage> getCustomerTransformedImages(final List<CustomerTransformedSaveImage> customerTransformedSaveImageList) {
        return customerTransformedSaveImageList.stream()
                .map(customerTransformedSaveImage -> new CustomerTransformedImage(customerTransformedSaveImage.id(), customerTransformedSaveImage.putUrl()))
                .toList();
    }

    private List<CustomerTransformedSaveImage> checkAndAddImages(final KitchenRegister kitchenRegister, final Long userId) {
        if (!kitchenRegister.kitchenImagesRegisterList().isEmpty()) {
            return customerMyPageQueryInputPort.postImages(
                    kitchenRegister.kitchenImagesRegisterList(),
                    userId);
        }
        return List.of();
    }

    private void checkAndAddTools(final KitchenRegister kitchenRegister, final Kitchen kitchen) {
        if (!kitchenRegister.kitchenToolsRegisterList().isEmpty()) {
            List<KitchenTools> kitchenToolsList = customerMyPageQueryInputPort.postTools(kitchenRegister.kitchenToolsRegisterList());

            kitchen.addTools(kitchenToolsList);
        }
    }

    @Transactional
    public CustomerImageListResponse editKitchen(final KitchenEdit kitchenEdit, final String token,
                                                 final Long kitchenId) {
        Long userId = jwtService.getJwtUserId(token);
        Kitchen kitchen = customerMyPageQueryInputPort.findByKitchen(kitchenId);

        checkKitchenCustomerAndJwtCustomer(userId, kitchen.getCustomer().getId());

        List<CustomerTransformedSaveImage> customerTransformedSaveImages = changeImages(kitchenEdit, kitchen);
        List<KitchenImages> kitchenImagesList = changeImages(customerTransformedSaveImages);
        addImages(kitchenImagesList, kitchen);

        changeTools(kitchenEdit, kitchen);

        kitchen.edit(kitchenEdit);

        List<CustomerTransformedImage> customerTransformedImageList = getCustomerTransformedImages(customerTransformedSaveImages);

        return new CustomerImageListResponse(customerTransformedImageList);
    }

    private List<KitchenImages> changeImages(final List<CustomerTransformedSaveImage> customerTransformedSaveImages) {
        if (customerTransformedSaveImages.isEmpty()) {
            return List.of();
        }

        return customerTransformedSaveImages.stream()
                .map(customerTransformedSaveImage -> KitchenImages.builder()
                        .order(customerTransformedSaveImage.id())
                        .imageName(customerTransformedSaveImage.getUrl())
                        .build())
                .toList();
    }

    private void addImages(final List<KitchenImages> kitchenImagesList,
                           final Kitchen kitchen) {
        if (!kitchenImagesList.isEmpty()) {
            kitchen.addImages(kitchenImagesList);
        }
    }

    private void checkKitchenCustomerAndJwtCustomer(final Long userId, final Long kitchenCustomerId) {
        if (!Objects.equals(userId, kitchenCustomerId)) {
            throw new NotEqualException(ErrorCode.CUSTOMER_KITCHEN_NOT_EQUALS);
        }
    }

    private List<CustomerTransformedSaveImage> changeImages(final KitchenEdit kitchenEdit, final Kitchen kitchen) {
        kitchen.getKitchenImagesList().clear();

        if (kitchenEdit.kitchenImagesRegisterList().isEmpty()) {
            return List.of();
        }

        return customerMyPageQueryInputPort.editImages(
                kitchenEdit.kitchenImagesRegisterList(),
                kitchen.getCustomer().getId());
    }

    private void changeTools(final KitchenEdit kitchenEdit, final Kitchen kitchen) {

        if (kitchenEdit.kitchenToolsRegisterList().isEmpty()) {
            return;
        }

        List<KitchenTools> kitchenToolsList = customerMyPageQueryInputPort.editTools(kitchenEdit.kitchenToolsRegisterList());
        kitchen.getKitchenToolsList().clear();
        kitchen.addTools(kitchenToolsList);
    }

    public CustomerKitchenResponse getKitchen(final String token, final Long kitchenId) {
        Long userId = jwtService.getJwtUserId(token);
        Kitchen kitchen = customerMyPageQueryInputPort.findByKitchen(kitchenId);

        checkKitchenCustomerAndJwtCustomer(userId, kitchen.getCustomer().getId());

        return CustomerKitchenResponse.builder()
                .nickName(kitchen.getNickName())
                .address(kitchen.getAddress())
                .addressSub(kitchen.getAddressSub())
                .burnerType(kitchen.getBurnerType())
                .burnerQuantity(kitchen.getBurnerQuantity())
                .kitchenImagesRegisterList(
                        Optional.ofNullable(kitchen.getKitchenImagesList())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(kitchenImages -> new KitchenImagesRegister(kitchenImages.getOrder(), kitchenImages.getImageName()))
                                .toList()
                )
                .kitchenToolsRegisterList(
                        kitchen.getKitchenToolsList().stream()
                                .map(kitchenTools -> new KitchenToolsRegister(kitchenTools.getToolName()))
                                .toList()
                )
                .additionalEquipment(kitchen.getAdditionalEquipment())
                .requirements(kitchen.getRequirements())
                .considerations(kitchen.getConsiderations())
                .build();
    }

    @Transactional
    public void editCustomer(final CustomerEdit customerEdit, final String token) {

        boolean hasValidPhone = Objects.nonNull(customerEdit.phone()) && !customerEdit.phone().isEmpty();
        boolean hasValidEmail = Objects.nonNull(customerEdit.email()) && !customerEdit.email().isEmpty();

        if (!hasValidPhone && !hasValidEmail) {
            throw new InvalidRequestException(ErrorCode.CUSTOMER_EDIT_INVALID_INPUT);
        }

        Long userId = jwtService.getJwtUserId(token);
        Customer customer = customerMyPageQueryInputPort.getCustomerProfile(userId);

        boolean phoneChangeCheck = updateField(
                customer::getCustomerPhone,
                customerEdit::phone,
                (phone, currentPhone) -> customerMyPageQueryInputPort.checkPhoneAndAuthCode(phone, customerEdit.phoneAuthNum(), CustomerVerificationStatus.EDIT),
                customerMyPageQueryInputPort::deleteCustomerPhone
        );

        boolean emailChangeCheck = updateField(
                customer::getCustomerEmail,
                customerEdit::email,
                (email, currentEmail) -> customerMyPageQueryInputPort.checkEmailAndAuthCode(email, customerEdit.emailAuthNum(), CustomerVerificationStatus.EDIT),
                customerMyPageQueryInputPort::deleteCustomerEmail
        );

        customer.edit(customerEdit, phoneChangeCheck, emailChangeCheck);
    }

    private boolean updateField(
            Supplier<String> currentValueSupplier,
            Supplier<String> newValueSupplier,
            BiConsumer<String, String> checkAndAuthFunction,
            Consumer<String> deleteFunction
    ) {
        String currentValue = currentValueSupplier.get();
        String newValue = newValueSupplier.get();

        if (Objects.nonNull(newValue) && !newValue.isEmpty()) {
            checkAndAuthFunction.accept(newValue, currentValue);
            deleteFunction.accept(newValue);
            return true;
        }

        return false;
    }

    public List<CustomerKitchenListResponse> getKitchenList(final String token) {
        Long userId = jwtService.getJwtUserId(token);

        List<CustomerKitchenListDto> customerKitchenListDtos = customerMyPageQueryInputPort.getKitchenAllList(userId);

        if (customerKitchenListDtos.isEmpty()) {
            return null;
        }

        return customerKitchenListDtos.stream()
                .map(kitchenListDto -> new CustomerKitchenListResponse(
                                kitchenListDto.id(),
                                kitchenListDto.kitchenMainStatus(),
                                kitchenListDto.nickName(),
                                kitchenListDto.address(),
                                kitchenListDto.addressSub(),
                                kitchenListDto.imageNames())
                ).toList();
    }

    @Transactional
    public void patchMainKitchen(final Long kitchenId, final String token) {
        Long userId = jwtService.getJwtUserId(token);

        List<Kitchen> kitchenList = customerMyPageQueryInputPort.getKitchenList(userId);
        kitchenList.stream()
                .filter(kitchen -> kitchen.getMainStatus() == KitchenMainStatus.MAIN)
                .findFirst()
                .ifPresent(kitchen -> kitchen.patchMain(KitchenMainStatus.NOT_MAIN));

        kitchenList.stream()
                .filter(kitchen -> kitchen.getKitchenId().equals(kitchenId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorCode.KITCHEN_NOT_FOUND))
                .patchMain(KitchenMainStatus.MAIN);
    }

    public CustomerMainKitchenResponse getMainKitchen(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        Kitchen kitchen = customerMyPageQueryInputPort.getMainKitchen(userId, KitchenMainStatus.MAIN);

        if (kitchen == null) {
            return null;
        }

        return CustomerMainKitchenResponse.builder()
                .id(kitchen.getKitchenId())
                .nickName(kitchen.getNickName())
                .address(kitchen.getAddress())
                .addressSub(kitchen.getAddressSub())
                .burnerType(kitchen.getBurnerType())
                .burnerQuantity(kitchen.getBurnerQuantity())
                .kitchenImagesRegisterList(Optional.ofNullable(kitchen.getKitchenImagesList())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(KitchenImages::getImageName)
                        .toList())
                .kitchenToolsRegisterList(kitchen.getKitchenToolsList().stream()
                .map(KitchenTools::getToolName)
                .toList())
                .requirements(kitchen.getRequirements())
                .considerations(kitchen.getConsiderations())
                .build();
    }
}
