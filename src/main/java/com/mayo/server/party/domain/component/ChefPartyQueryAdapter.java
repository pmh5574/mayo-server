package com.mayo.server.party.domain.component;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.chef.domain.repository.ChefJpaRepository;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.party.app.port.in.ChefPartyQueryInputPort;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.domain.model.PartySchedule;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class ChefPartyQueryAdapter implements ChefPartyQueryInputPort {

    private final ChefJpaRepository chefJpaRepository;

    @Override
    public Chef findByChef(final Long chefId) {
        return chefJpaRepository.findById(chefId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHEF_NOT_FOUND));
    }

    @Override
    public List<ChefNotSelectedDto> getChefNotSelectedList(final List<PartySchedule> partyScheduleList) {
        return chefJpaRepository.getChefNotSelectedList(partyScheduleList);
    }
}
