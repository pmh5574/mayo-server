package com.mayo.server.party.app.service;

import com.mayo.server.party.adapter.in.web.CustomerPartySearchRequest;
import com.mayo.server.party.app.port.in.CustomerBoardQueryInputPort;
import com.mayo.server.party.app.port.out.ChefSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomerBoardService {

    private final CustomerBoardQueryInputPort customerBoardQueryInputPort;

    public List<ChefSearch> getSearchChefAll(final CustomerPartySearchRequest customerPartySearchRequest) {
        return customerBoardQueryInputPort.searchChefAll(customerPartySearchRequest.categories(), customerPartySearchRequest.services(), customerPartySearchRequest.areas());
    }
}
