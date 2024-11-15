package com.mayo.server.customer.adapter;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.customer.adapter.in.web.CustomerEmailRegisterRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLoginRequest;
import com.mayo.server.customer.adapter.in.web.CustomerLogoutRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPasswordChange;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneRegisterRequest;
import com.mayo.server.customer.adapter.in.web.ReissueAccessToken;
import com.mayo.server.customer.app.service.CustomerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    private final CustomerAuthService customerAuthService;

    @Operation(summary = "고객 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/login")
    public Callable<Response<BaseResponse>> login(@RequestBody @Validated CustomerLoginRequest customerLoginRequest,
                                              HttpServletResponse res) {
        JwtToken jwtToken = customerAuthService.login(customerLoginRequest);

        res.setHeader("Authorization", jwtToken.accessToken());
        res.setHeader("RefreshToken", jwtToken.refreshToken());
        return Response::new;
    }

    @Operation(summary = "고객 로그아웃")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/logout")
    public Callable<Response<BaseResponse>> logout(@RequestBody @Validated CustomerLogoutRequest customerLogoutRequest) {
        customerAuthService.logout(customerLogoutRequest);
        return Response::new;
    }

    @Operation(summary = "고객 토큰 재발행")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/reissue-access-token")
    public Callable<Response<BaseResponse>> reissueAccessToken(@RequestBody @Validated ReissueAccessToken reissueAccessToken,
                                                           HttpServletResponse res) {
        JwtToken jwtToken = customerAuthService.reissueAccessToken(reissueAccessToken);
        res.setHeader("Authorization", jwtToken.accessToken());
        res.setHeader("RefreshToken", jwtToken.refreshToken());
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/phone")
    public Callable<Response<BaseResponse>> postCustomerByPhone(@RequestBody @Validated CustomerPhoneRegisterRequest customerPhoneRegisterRequest){
        customerAuthService.registerByPhone(customerPhoneRegisterRequest);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/email")
    public Callable<Response<BaseResponse>> postCustomerByEmail(@RequestBody @Validated CustomerEmailRegisterRequest customerEmailRegisterRequest){
        customerAuthService.registerByEmail(customerEmailRegisterRequest);
        return Response::new;
    }

    @Operation(summary = "고객 비멀번호 변경")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PatchMapping("/password")
    public Callable<Response<BaseResponse>> patchCustomerPasswordChange(@RequestBody @Validated CustomerPasswordChange customerPasswordChange){
        customerAuthService.patchPasswordChange(customerPasswordChange);
        return Response::new;
    }

}
