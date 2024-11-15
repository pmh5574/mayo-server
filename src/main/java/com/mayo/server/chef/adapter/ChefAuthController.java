package com.mayo.server.chef.adapter;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.chef.adapter.in.web.LoginRequest;
import com.mayo.server.chef.adapter.in.web.RegisterEmailRequest;
import com.mayo.server.chef.adapter.in.web.RegisterPhoneRequest;
import com.mayo.server.chef.adapter.in.web.UpdatePwdRequest;
import com.mayo.server.chef.app.port.in.ChefAuthUseCase;
import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PasswordAccess;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@Tag(name = "Chef Auth", description = "Chef Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chef/auth")
public class ChefAuthController {

    private final ChefAuthUseCase chefAuthUseCase;

    @Operation(summary = "요리사 정보")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @GetMapping("/info")
    public Callable<Response<Object>> getChefInfo(
            HttpServletRequest request
    ) {

        return Response::new;
    }

    @Operation(summary = "요리사 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/phone")
    public Callable<Response<Object>> postRegister(
            @Valid @RequestBody RegisterPhoneRequest req
    ) {

        chefAuthUseCase.registerByPhone(req);

        return Response::new;
    }

    @Operation(summary = "요리사 회원가입")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/register/email")
    public Callable<Response<Object>> postRegister(
            @Valid @RequestBody RegisterEmailRequest req
    ) {

        chefAuthUseCase.registerByEmail(req);

        return Response::new;
    }

    @Operation(summary = "요리사 로그인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/login")
    public Callable<Response<Object>> postLogin(
            @Valid @RequestBody LoginRequest req,
            HttpServletResponse res
    ) {

        JwtToken tokens = chefAuthUseCase.login(req);

        res.setHeader("Authorization", tokens.accessToken());
        res.setHeader("RefreshToken", tokens.refreshToken());

        return Response::new;
    }

    @PasswordAccess
    @Operation(summary = "요리사 비밀번호 재변경")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PatchMapping("/pwd")
    public Callable<Response<Object>> updateChefPwd(
            @Valid @RequestBody UpdatePwdRequest req
    ) {

        chefAuthUseCase.updateChefPwd(req);

        return Response::new;
    }

}