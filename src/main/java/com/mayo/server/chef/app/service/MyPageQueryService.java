package com.mayo.server.chef.app.service;

import com.mayo.server.chef.adapter.out.persistence.MyPageResponse;
import com.mayo.server.chef.adapter.out.persistence.ProfileResponse;
import com.mayo.server.chef.app.port.out.ChefQueryOutputPort;
import com.mayo.server.chef.app.port.out.MyPageOutputPort;
import com.mayo.server.chef.app.port.out.MyPageQueryUseCase;
import com.mayo.server.chef.domain.model.*;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotFoundException;
import com.mayo.server.common.utility.CommonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageQueryService implements MyPageQueryUseCase {

    private final ChefQueryOutputPort query;
    private final MyPageOutputPort myPageQuery;
    @Override
    public MyPageResponse profile(String id) {

        Chef chef = query.getChefById(id);
        if(Objects.isNull(chef)) {
            throw new NotFoundException(ErrorCode.CHEF_NOT_FOUND);
        }

        return new MyPageResponse(
                chef.getChefName(),
                chef.getChefBirthday(),
                Objects.isNull(chef.getChefPhone()) ? "" : chef.getChefPhone(),
                Objects.isNull(chef.getChefEmail()) ? "" : chef.getChefEmail()
        );
    }

    @Override
    public ProfileResponse activeProfile(Long id) {

        ChefInformation chefInformation = myPageQuery.getChefInformation(id);
        if(Objects.isNull(chefInformation)) {
            chefInformation = new ChefInformation();
        }

        List<ChefHashTag> hashTags = myPageQuery.getChefHashTags(id);

        List<ChefPortfolio> portfolios = myPageQuery.getChefPortPolio(id);

        List<ChefLicense> licenses = myPageQuery.getChefLicense(id);

        List<String> serviceName = myPageQuery.getChefServiceList(id);

        return new ProfileResponse(
                chefInformation.getChefInfoExperienceOrDefault(),
                chefInformation.getChefInfoIntroduceOrDefault(),
                ChefHashTag.getHashTags(hashTags),
                chefInformation.getChefInfoRegionOrDefault(),
                chefInformation.getChefInfoDescriptionOrDefault(),
                chefInformation.getChefInfoAdditionalOrDefault(),
                chefInformation.getHopePayOrDefault(),
                chefInformation.getMinServiceTimeOrDefault(),
                serviceName.isEmpty() ? List.of() : serviceName,
                ChefPortfolio.getPortFolios(portfolios),
                ChefLicense.getLicenses(licenses)
        );
    }
}
