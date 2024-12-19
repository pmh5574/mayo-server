package com.mayo.server.customer.adapter;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.customer.adapter.in.web.CustomerEmailRegisterRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLoginRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLogoutRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPasswordChangeRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneRegisterRequest;
import com.mayo.server.customer.adapter.in.web.ReissueAccessToken;
import com.mayo.server.customer.app.port.in.CustomerAuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Auth", description = "Customer Auth API")
@RequiredArgsConstructor
@RequestMapping("/customer/auth")
@RestController
public class CustomerAuthController {

    private final CustomerAuthUseCase customerAuthUserCase;

    @Operation(summary = "고객 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/login")
    public Callable<Response<BaseResponse>> login(@RequestBody @Valid CustomerLoginRequest customerLoginRequest,
                                              HttpServletResponse res) {
        JwtToken jwtToken = customerAuthUserCase.login(customerLoginRequest.toServiceRequest());

        res.setHeader("Authorization", jwtToken.accessToken());
        res.setHeader("RefreshToken", jwtToken.refreshToken());
        return Response::new;
    }

    @Operation(summary = "고객 로그아웃")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/logout")
    public Callable<Response<BaseResponse>> logout(@RequestBody @Valid CustomerLogoutRequest customerLogoutRequest) {
        customerAuthUserCase.logout(customerLogoutRequest);
        return Response::new;
    }

    @Operation(summary = "고객 토큰 재발행")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/reissue-access-token")
    public Callable<Response<BaseResponse>> reissueAccessToken(@RequestBody @Valid ReissueAccessToken reissueAccessToken,
                                                           HttpServletResponse res) {
        JwtToken jwtToken = customerAuthUserCase.reissueAccessToken(reissueAccessToken);
        res.setHeader("Authorization", jwtToken.accessToken());
        res.setHeader("RefreshToken", jwtToken.refreshToken());
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/phone")
    public Callable<Response<BaseResponse>> postCustomerByPhone(@RequestBody @Valid CustomerPhoneRegisterRequest customerPhoneRegisterRequest){
        customerAuthUserCase.registerByPhone(customerPhoneRegisterRequest.toServiceRequest());
        return Response::new;
    }

    @Operation(summary = "고객 이메일 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/email")
    public Callable<Response<BaseResponse>> postCustomerByEmail(@RequestBody @Valid CustomerEmailRegisterRequest customerEmailRegisterRequest){
        customerAuthUserCase.registerByEmail(customerEmailRegisterRequest.toServiceRequest());
        return Response::new;
    }

    @Operation(summary = "고객 비멀번호 변경")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PatchMapping("/password")
    public Callable<Response<BaseResponse>> patchCustomerPasswordChange(@RequestBody @Valid CustomerPasswordChangeRequest customerPasswordChangeRequest){
        customerAuthUserCase.patchPasswordChange(customerPasswordChangeRequest.toServiceRequest());
        return Response::new;
    }

}
