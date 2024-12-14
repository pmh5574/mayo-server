package com.mayo.server.customer.app.port.in;

public record CustomerTransformedSaveImage(
        Integer id,
        String getUrl,
        String putUrl
) {
}
