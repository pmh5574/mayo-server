package com.mayo.server.customer.adapter.out.persistence;

import com.mayo.server.customer.app.port.in.CustomerTransformedImage;
import java.util.List;

public record CustomerImageListResponse(List<CustomerTransformedImage> kitchenImagesList) {
}
