package com.mayo.server.party.app.port.in;

import com.mayo.server.party.app.port.out.ChefSearch;
import java.util.List;

public interface CustomerBoardQueryInputPort {
    List<ChefSearch> searchChefAll(List<String> categories, List<String> services, List<String> areas);
}
