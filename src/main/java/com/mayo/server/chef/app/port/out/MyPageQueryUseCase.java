package com.mayo.server.chef.app.port.out;

import com.mayo.server.chef.adapter.out.persistence.MyPageResponse;
import com.mayo.server.chef.adapter.out.persistence.ProfileResponse;

public interface MyPageQueryUseCase {

    MyPageResponse profile(String id);

    ProfileResponse activeProfile(Long id);

}
