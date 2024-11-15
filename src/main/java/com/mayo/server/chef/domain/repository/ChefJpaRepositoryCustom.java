package com.mayo.server.chef.domain.repository;

import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.ChefSearch;
import com.mayo.server.party.domain.model.PartySchedule;
import java.util.List;

public interface ChefJpaRepositoryCustom {

    List<ChefSearch> getSearchListAll(String keyword);

    List<ChefNotSelectedDto> getChefNotSelectedList(List<PartySchedule> partyScheduleList);
}
