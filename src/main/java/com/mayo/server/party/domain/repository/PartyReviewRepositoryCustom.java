package com.mayo.server.party.domain.repository;

import com.mayo.server.party.app.port.out.PartyReviewListDto;
import java.util.List;

public interface PartyReviewRepositoryCustom {

    List<PartyReviewListDto> getReviewList(Long userId);
}
