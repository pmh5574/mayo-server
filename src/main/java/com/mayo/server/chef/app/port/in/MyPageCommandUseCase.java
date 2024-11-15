package com.mayo.server.chef.app.port.in;

import com.mayo.server.chef.adapter.in.web.MyPageRequest;
import com.mayo.server.chef.adapter.out.persistence.ProfileUpdateResponse;

public interface MyPageCommandUseCase {

    void updateMyPage(MyPageRequest.ChefInfoRequest req, Long id);

    ProfileUpdateResponse updateProfile(MyPageRequest.ProfileRequest req, Long id);

}
