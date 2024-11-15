package com.mayo.server.chef.app.port.in;

import com.mayo.server.chef.adapter.in.web.MyPageRequest;
import com.mayo.server.chef.domain.model.ChefInformation;

import java.util.List;

public interface MyPageInputPort {

    void updateMyPage(String name, String birthday, String email, String phone, Long id);

    void updateMyPage(String name, String birthday, String email, Long id, boolean isEmail);

    void updateMyPage(String name, String birthday, String phone, Long id);

    void updateMyPage(String name, String birthday, Long id);

    void postChefInfo(
            ChefInformation chefInformation
    );

    void postHashTag(Long id, List<String> tag);

    void postPortfolio(Long id, List<TransformedImageDto> images);

    void postLicense(Long id, List<TransformedImageDto> images);

    void deletePortfolio(Long id);

    void deleteLicense(Long id);

    void deleteByHashTags(Long id);

    void postServiceList(Long id, List<String> serviceList, String createdAt);

    void deleteServiceList(Long id);
}
