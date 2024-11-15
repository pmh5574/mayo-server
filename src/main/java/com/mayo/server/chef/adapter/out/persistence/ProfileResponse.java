package com.mayo.server.chef.adapter.out.persistence;

import com.mayo.server.chef.app.port.in.TransformedImageDto;

import java.util.List;

public record ProfileResponse (

        String experience,

        String introduce,

        List<String> tags,

        String activeRegion,

        String description,

        String additionalInfo,

        Long hopePay,

        Integer minServiceTime,

        List<String> serviceList,

        List<TransformedImageDto> portFolio,

        List<TransformedImageDto> license


) {
}
