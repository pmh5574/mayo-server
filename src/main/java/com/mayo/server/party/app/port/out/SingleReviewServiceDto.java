package com.mayo.server.party.app.port.out;

import com.mayo.server.party.domain.enums.ReviewServicesReason;

public interface SingleReviewServiceDto {
    Long partyReviewServiceId();
    String getServiceReason();
}
