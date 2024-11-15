package com.mayo.server.customer.app.port.in;

import com.mayo.server.customer.adapter.in.web.KitchenImagesRegister;
import java.util.List;

public interface CustomerS3InputPort {

    String getImagePrefix(KitchenImagesRegister kitchenImagesRegister, Long userId);

    List<CustomerTransformedImage> postKitchenImages(List<KitchenImagesRegister> kitchenImagesRegisters, Long userId);

    void deleteS3Images(Long userId);
}
