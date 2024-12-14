package com.mayo.server.customer.domain.componet;

import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.UserType;
import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import com.mayo.server.customer.app.port.in.CustomerS3InputPort;
import com.mayo.server.customer.app.port.in.CustomerTransformedSaveImage;
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
    public List<CustomerTransformedSaveImage> postKitchenImages(final List<KitchenImagesRegister> kitchenImagesRegisters, final Long userId) {
        return kitchenImagesRegisters.stream()
                .map(kitchenImagesRegister -> {

                    String imagePrefix = getImagePrefix(kitchenImagesRegister, userId);
                    customerS3Client.getPreSignedUrl(imagePrefix);

                    return new CustomerTransformedSaveImage(
                            kitchenImagesRegister.id(),
                            getCloudFrontSignedUrl(imagePrefix),
                            customerS3Client.getPreSignedUrl(imagePrefix)
                    );
                })
                .toList();
    }

    @Override
    public String getCloudFrontSignedUrl(String imageKey) {
        return AWS_CF_DISTRIBUTION + "/" + imageKey;
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
