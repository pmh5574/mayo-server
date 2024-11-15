package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.app.port.out.MyPageOutputPort;
import com.mayo.server.chef.domain.model.*;
import com.mayo.server.chef.domain.repository.*;
import com.mayo.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class MyPageQueryAdapter implements MyPageOutputPort {

    private final InformationJpaRepository informationJpaRepository;
    private final HashTagJpaRepository hashTagJpaRepository;
    private final PortfolioJpaRepository portfolioJpaRepository;
    private final LicenseJpaRepository licenseJpaRepository;
    private final ChefServiceRepository chefServiceRepository;

    @Override
    public ChefInformation getChefInformation(Long id) {
        return informationJpaRepository.findByChefId(id);
    }

    @Override
    public List<ChefHashTag> getChefHashTags(Long id) {
        return hashTagJpaRepository.findAllByChefId(id);
    }

    @Override
    public List<ChefPortfolio> getChefPortPolio(Long id) {
        return portfolioJpaRepository.findAllByChefId(id);
    }

    @Override
    public List<ChefLicense> getChefLicense(Long id) {
        return licenseJpaRepository.findAllByChefId(id);
    }

    @Override
    public List<String> getChefServiceList(Long id) {
        return chefServiceRepository.findAllServiceNames(id);
    }
}
