package com.mayo.server.customer.app.port.in;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.customer.adapter.in.web.CustomerLogoutRequest;
import com.mayo.server.customer.adapter.in.web.ReissueAccessToken;
import com.mayo.server.customer.app.port.in.request.CustomerEmailRegisterServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerLoginServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPasswordChangeServiceRequest;
import com.mayo.server.customer.app.port.in.request.CustomerPhoneRegisterServiceRequest;

public interface CustomerAuthUseCase {
    JwtToken login(CustomerLoginServiceRequest serviceRequest);

    void logout(CustomerLogoutRequest customerLogoutRequest);

    JwtToken reissueAccessToken(ReissueAccessToken reissueAccessToken);

    void registerByPhone(CustomerPhoneRegisterServiceRequest serviceRequest);

    void registerByEmail(CustomerEmailRegisterServiceRequest request);

    void patchPasswordChange(CustomerPasswordChangeServiceRequest request);
}
