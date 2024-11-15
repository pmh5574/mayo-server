package com.mayo.server.chef.app.port.in;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.chef.adapter.in.web.LoginRequest;
import com.mayo.server.chef.adapter.in.web.RegisterEmailRequest;
import com.mayo.server.chef.adapter.in.web.RegisterPhoneRequest;
import com.mayo.server.chef.adapter.in.web.UpdatePwdRequest;

public interface ChefAuthUseCase {

    void registerByPhone(RegisterPhoneRequest req);

    void registerByEmail(RegisterEmailRequest req);

    JwtToken login(LoginRequest req);

    void updateChefPwd(UpdatePwdRequest req);
}
