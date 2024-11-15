package com.mayo.server.chef.app.port.out;

import com.mayo.server.chef.domain.model.*;

import java.util.List;

public interface MyPageOutputPort {

    ChefInformation getChefInformation(Long id);

    List<ChefHashTag> getChefHashTags(Long id);

    List<ChefPortfolio> getChefPortPolio(Long id);

    List<ChefLicense> getChefLicense(Long id);

    List<String> getChefServiceList(Long id);

}
