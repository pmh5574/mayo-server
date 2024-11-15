package com.mayo.server.chef.adapter;

import com.mayo.server.auth.adapter.out.persistence.JwtToken;
import com.mayo.server.chef.adapter.in.web.EmailRequest;
import com.mayo.server.chef.adapter.in.web.EmailVerifyRequest;
import com.mayo.server.chef.adapter.in.web.PhoneRequest;
import com.mayo.server.chef.adapter.in.web.PhoneVerifyRequest;
import com.mayo.server.chef.adapter.out.persistence.FindIdResponse;
import com.mayo.server.chef.app.port.in.VerifyUseCase;
import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.common.swagger.BaseStringResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@Tag(name = "Chef Verify", description = "Chef Verify API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chef/verify")
public class VerifyController {

    private final VerifyUseCase verifyUseCase;

    @Operation(summary = "요리사 전화번호 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/register")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody PhoneRequest.RegisterRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 아이디 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/username")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody PhoneRequest.UsernameRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 비밀번호 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/pwd")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody PhoneRequest.PwdRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 마이페이지 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PatchMapping("/phone/my-page")
    public Callable<Response<String>> postSend(
            @Valid @RequestBody PhoneRequest.MyPageRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 아이디 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseStringResponse.class))})
    @PublicAccess
    @PatchMapping("/phone/username")
    public Callable<Response<FindIdResponse>> updateVerify(
            @Valid @RequestBody PhoneVerifyRequest.UsernameRequest req
    ) {

        FindIdResponse username = verifyUseCase.verify(req);

        return () -> new Response<>(username);
    }

    @Operation(summary = "요리사 비밀번호 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PatchMapping("/phone/pwd")
    public Callable<Response<String>> updateVerify(
            @Valid @RequestBody PhoneVerifyRequest.PwdRequest req,
            HttpServletResponse res
    ) {

        JwtToken tokens = verifyUseCase.verify(req);

        res.setHeader("Password", tokens.accessToken());

        return Response::new;
    }

    @Operation(summary = "요리사 이메일 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/register")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody EmailRequest.RegisterRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 아이디 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/username")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody EmailRequest.UsernameRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 비밀번호 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/pwd")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody EmailRequest.PwdRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 이메일 마이페이지 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/my-page")
    public Callable<Response<BaseResponse>> postSend(
            @Valid @RequestBody EmailRequest.MyPageRequest req
    ) {

        verifyUseCase.send(req);

        return Response::new;
    }

    @Operation(summary = "요리사 아이디 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseStringResponse.class))})
    @PublicAccess
    @PatchMapping("/email/username")
    public Callable<Response<FindIdResponse>> updateVerify(
            @Valid @RequestBody EmailVerifyRequest.UsernameRequest req
    ) {

        FindIdResponse username = verifyUseCase.verify(req);

        return () -> new Response<>(username);
    }

    @Operation(summary = "요리사 비밀번호 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PatchMapping("/email/pwd")
    public Callable<Response<String>> updateVerify(
            @Valid @RequestBody EmailVerifyRequest.PwdRequest req,
            HttpServletResponse res
    ) {

        JwtToken tokens = verifyUseCase.verify(req);

        res.setHeader("Password", tokens.accessToken());

        return Response::new;
    }

}
