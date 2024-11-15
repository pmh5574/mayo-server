package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.app.port.in.MyPageInputPort;
import com.mayo.server.chef.app.port.in.TransformedImageDto;
import com.mayo.server.chef.domain.model.*;
import com.mayo.server.chef.domain.repository.*;
import com.mayo.server.common.Constants;
import com.mayo.server.common.annotation.Adapter;
import com.mayo.server.common.utility.DateUtility;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Adapter
@RequiredArgsConstructor
public class MyPageCommandAdapter implements MyPageInputPort {

    private final ChefJpaRepository chefJpaRepository;
    private final InformationJpaRepository informationJpaRepository;
    private final PortfolioJpaRepository portfolioJpaRepository;
    private final LicenseJpaRepository licenseJpaRepository;
    private final HashTagJpaRepository hashTagJpaRepository;
    private final ChefServiceRepository chefServiceRepository;

    @Override
    public void updateMyPage(String name, String birthday, String email, String phone, Long id) {
        chefJpaRepository.updateChefInfoAll(
                name,
                birthday,
                email,
                phone,
                id
        );
    }

    @Override
    public void updateMyPage(String name, String birthday, String email, Long id, boolean isEmail) {
        chefJpaRepository.updateChefInfoEmail(
                name,
                birthday,
                email,
                id
        );
    }

    @Override
    public void updateMyPage(String name, String birthday, String phone, Long id) {
        chefJpaRepository.updateChefInfoPhone(
                name, birthday, phone, id);
    }

    @Override
    public void updateMyPage(String name, String birthday, Long id) {
        chefJpaRepository.updateChefInfoNormal(
                name, birthday, id);
    }

    @Override
    public void postChefInfo(ChefInformation chefInformation) {
        informationJpaRepository.save(
                chefInformation
        );
    }

    @Override
    public void postHashTag(Long id, List<String> tag) {

        ArrayList<ChefHashTag> tags = new ArrayList<>();

        tag.forEach(
                v -> {
                    tags.add(
                            ChefHashTag
                                    .builder()
                                    .chefId(id)
                                    .chefHashTag(v)
                                    .build()
                    );
                }
        );

        hashTagJpaRepository.saveAll(tags);
    }

    @Override
    public void postPortfolio(Long id, List<TransformedImageDto> images) {

        List<ChefPortfolio> portfolios = images.stream().map(v -> ChefPortfolio
                .builder()
                .chefId(id)
                .chefPortfolioImage(v.url())
                .deletedAt(null)
                .order(v.id())
                .build()).toList();

        portfolioJpaRepository.saveAll(portfolios);
    }

    @Override
    public void postLicense(Long id, List<TransformedImageDto> images) {

        ArrayList<ChefLicense> licenses = new ArrayList<>();

        images.forEach(v -> {

            licenses.add(
                    ChefLicense
                            .builder()
                            .chefId(id)
                            .order(v.id())
                            .chefLicense(" ")
                            .chefLicenceImage(v.url())
                            .deletedAt(null)
                            .build()
            );
        });

        licenseJpaRepository.saveAll(licenses);

    }

    @Override
    public void deletePortfolio(Long id) {
        portfolioJpaRepository.deleteById(id, DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss));
    }

    @Override
    public void deleteLicense(Long id) {
        licenseJpaRepository.deleteById(id, DateUtility.getUTC0String(Constants.yyyy_MM_DD_HH_mm_ss));
    }

    @Override
    public void deleteByHashTags(Long id) {
        hashTagJpaRepository.deletedByChefId(id);
    }

    @Override
    public void postServiceList(Long id, List<String> serviceList, String createdAt) {

        List<ChefService> chefServices = new ArrayList<>();

        for (String s : serviceList) {
            chefServices.add(
                    ChefService.builder().serviceName(s).chefId(id).createdAt(createdAt).build()
            );
        }

        chefServiceRepository.saveAll(chefServices);

    }

    @Override
    public void deleteServiceList(Long id) {

        chefServiceRepository.deleteByChefId(id);
    }


}
