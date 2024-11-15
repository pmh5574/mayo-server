package com.mayo.server.chef.adapter.out.persistence;

import com.mayo.server.chef.app.port.in.TransformedImageDto;

import java.util.List;

public record ProfileUpdateResponse(

        List<TransformedImageDto> portFolioUrls,

        List<TransformedImageDto> licenseUrls
) {
}
