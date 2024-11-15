package com.mayo.server.party.domain.component;

import com.mayo.server.chef.domain.repository.ChefJpaRepository;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.party.app.port.in.CustomerBoardQueryInputPort;
import com.mayo.server.party.app.port.out.ChefSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class CustomerBoardQueryAdapter implements CustomerBoardQueryInputPort {

    private final ChefJpaRepository chefJpaRepository;

    @Override
    public List<ChefSearch> searchChefAll(final String keyword) {
        return chefJpaRepository.getSearchListAll(keyword);
    }
}
