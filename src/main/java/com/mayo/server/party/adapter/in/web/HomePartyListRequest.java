package com.mayo.server.party.adapter.in.web;

import java.util.List;

public record HomePartyListRequest(

        List<String> categoryList,

        List<String> serviceList,

        List<String> regionList
) {
}

