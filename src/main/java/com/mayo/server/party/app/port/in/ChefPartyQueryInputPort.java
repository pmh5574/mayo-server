package com.mayo.server.party.app.port.in;

import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.domain.model.PartySchedule;
import java.util.List;

public interface ChefPartyQueryInputPort {

    Chef findByChef(Long chefId);

    List<ChefNotSelectedDto> getChefNotSelectedList(List<PartySchedule> partyScheduleList);
}
