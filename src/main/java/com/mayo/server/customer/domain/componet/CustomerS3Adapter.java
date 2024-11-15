package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.app.port.in.CustomerTransformedImage;
import com.mayo.server.customer.app.port.in.CustomerS3InputPort;
import com.mayo.server.customer.infra.CustomerS3Client;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Adapter
public class CustomerS3Adapter implements CustomerS3InputPort {

    private final CustomerS3Client customerS3Client;

    @Value("${spring.aws.credentials.cf}")
    private String AWS_CF_DISTRIBUTION;

    @Override
    public List<CustomerTransformedImage> postKitchenImages(final List<KitchenImagesRegister> kitchenImagesRegisters, final Long userId) {
        return kitchenImagesRegisters.stream()
                .map(kitchenImagesRegister ->
                        new CustomerTransformedImage(
                                kitchenImagesRegister.id(),
                                customerS3Client.getPreSignedUrl(getImagePrefix(kitchenImagesRegister, userId))))
                .toList();
    }

    @Override
    public void deleteS3Images(Long userId) {
        List<String> keyList = customerS3Client.listObjectKeys(UserType.CUSTOMER + "/" + userId + "/");
        keyList.forEach(customerS3Client::deleteObjectS3);
    }

    @Override
    public String getImagePrefix(final KitchenImagesRegister kitchenImagesRegister, final Long userId) {

        return UserType.CUSTOMER + "/" + userId + "/" + kitchenImagesRegister.id() + "_" + kitchenImagesRegister.key();
    }
}
