package com.mayo.server.party.app.service;

import com.mayo.server.auth.app.service.JwtService;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.DuplicateException;
import com.mayo.server.party.adapter.in.web.PartyReviewRegisterRequest;
import com.mayo.server.party.app.port.in.PartyReviewQueryInputPort;
import com.mayo.server.party.app.port.out.PartyReviewListDto;
import com.mayo.server.party.domain.enums.HomePartyStatus;
import com.mayo.server.party.domain.model.CustomerHomeParty;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartyReviewService {

    private final JwtService jwtService;
    private final PartyReviewQueryInputPort partyReviewQueryInputPort;

    @Transactional
    public void saveReview(final PartyReviewRegisterRequest partyReviewRegisterRequest, final String token) {
        Long userId = jwtService.getJwtUserId(token);
        CustomerHomeParty customerHomeParty = findByCustomerHomeParty(partyReviewRegisterRequest.homePartyId(),
                userId);
        checkReviewExists(customerHomeParty);
        partyReviewQueryInputPort.saveReview(partyReviewRegisterRequest, customerHomeParty);
    }
    private void checkReviewExists(final CustomerHomeParty customerHomeParty) {
        if (partyReviewQueryInputPort.checkReviewExists(customerHomeParty)) {
            throw new DuplicateException(ErrorCode.REVIEW_DUPLICATED);
        }
    }
    private CustomerHomeParty findByCustomerHomeParty(final Long homePartyId, final Long userId) {
        return partyReviewQueryInputPort.findByCustomerHomeParty(homePartyId, userId, HomePartyStatus.FINISH);
    }

    public List<PartyReviewListDto> getReviewList(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        return partyReviewQueryInputPort.getReviewList(userId);
    }
}
