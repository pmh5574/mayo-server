package com.mayo.server.customer.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.customer.adapter.in.web.CustomerEmailVerifyEditRequest;
import com.mayo.server.customer.adapter.in.web.CustomerEmailVerifyRegisterRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneVerifyEditRequest;
import com.mayo.server.customer.adapter.in.web.CustomerPhoneVerifyRegisterRequest;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByEmail;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByEmailCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByPhone;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerIdByPhoneCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByEmail;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByEmailCheck;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByPhone;
import com.mayo.server.customer.adapter.in.web.VerifyCustomerPasswordByPhoneCheck;
import com.mayo.server.customer.adapter.in.web.VerifyEmailAndCodeAndUserTypeForPasswordChange;
import com.mayo.server.customer.adapter.in.web.VerifyPhoneAndCodeAndUserTypeForPasswordChange;
import com.mayo.server.customer.adapter.out.persistence.CustomerVerifyIdByEmail;
import com.mayo.server.customer.adapter.out.persistence.CustomerVerifyIdByPhone;
import com.mayo.server.customer.app.service.CustomerVerifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Verify", description = "Customer Verify API")
@RequiredArgsConstructor
@RequestMapping("/customer/verify")
@RestController
public class CustomerVerifyController {

    private final CustomerVerifyService customerVerifyService;

    @Operation(summary = "고객 휴대폰 회원가입시 인증번호")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/register")
    public Callable<Response<BaseResponse>> registerByPhoneSend(@RequestBody @Validated CustomerPhoneVerifyRegisterRequest customerPhoneVerifyRegisterRequest) {
        customerVerifyService.registerByPhone(customerPhoneVerifyRegisterRequest);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 회원가입시 인증번호")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/register")
    public Callable<Response<BaseResponse>> registerByEmailSend(@RequestBody @Validated CustomerEmailVerifyRegisterRequest customerEmailVerifyRegisterRequest) {
        customerVerifyService.registerByEmail(customerEmailVerifyRegisterRequest);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 아이디 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/username")
    public Callable<Response<BaseResponse>> verifyCustomerIdByPhone(@RequestBody @Validated final VerifyCustomerIdByPhone verifyCustomerIdByPhone){
        customerVerifyService.findIdByPhone(verifyCustomerIdByPhone);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 아이디 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/username/check")
    public Callable<Response<CustomerVerifyIdByPhone>> verifyCustomerIdByPhoneCheck(@RequestBody @Validated final VerifyCustomerIdByPhoneCheck verifyCustomerIdByPhoneCheck){
        return () -> new Response<>(customerVerifyService.findIdByPhoneCheck(verifyCustomerIdByPhoneCheck));
    }

    @Operation(summary = "고객 이메일 아이디 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/username")
    public Callable<Response<BaseResponse>> verifyCustomerIdByEmail(@RequestBody @Validated final VerifyCustomerIdByEmail verifyCustomerIdByEmail){
        customerVerifyService.findIdByEmail(verifyCustomerIdByEmail);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 아이디 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/username/check")
    public Callable<Response<CustomerVerifyIdByEmail>> verifyCustomerIdByEmailCheck(@RequestBody @Validated final VerifyCustomerIdByEmailCheck verifyCustomerIdByEmailCheck){
        return () -> new Response<>(customerVerifyService.findIdByEmailCheck(verifyCustomerIdByEmailCheck));
    }

    @Operation(summary = "고객 휴대폰 비밀번호 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/password")
    public Callable<Response<BaseResponse>> verifyCustomerPasswordByPhone(@RequestBody @Validated final VerifyCustomerPasswordByPhone verifyCustomerPasswordByPhone){
        customerVerifyService.findPasswordByPhone(verifyCustomerPasswordByPhone);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 비밀번호 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/password/check")
    public Callable<Response<BaseResponse>> verifyCustomerPasswordByPhoneCheck(@RequestBody @Validated final VerifyCustomerPasswordByPhoneCheck verifyCustomerPasswordByPhoneCheck){
        customerVerifyService.findPasswordByPhoneCheck(verifyCustomerPasswordByPhoneCheck);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 비밀번호 변경 페이지 인증")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/password/phone")
    public Callable<Response<BaseResponse>> verifyPhoneAndCodeAndUserTypeForPasswordChange(@RequestBody @Validated VerifyPhoneAndCodeAndUserTypeForPasswordChange verifyPhoneAndCodeAndUserTypeForPasswordChange) {
        customerVerifyService.verifyPhoneAndCodeAndUserTypeForPasswordChange(verifyPhoneAndCodeAndUserTypeForPasswordChange);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 비밀번호 찾기 인증번호 발송")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/password")
    public Callable<Response<BaseResponse>> verifyCustomerPasswordByEmail(@RequestBody @Validated final VerifyCustomerPasswordByEmail verifyCustomerPasswordByEmail){
        customerVerifyService.findPasswordByEmail(verifyCustomerPasswordByEmail);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 비밀번호 찾기 인증번호 확인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/password/check")
    public Callable<Response<BaseResponse>> verifyCustomerPasswordByEmailCheck(@RequestBody @Validated final VerifyCustomerPasswordByEmailCheck verifyCustomerPasswordByEmailCheck){
        customerVerifyService.findPasswordByEmailCheck(verifyCustomerPasswordByEmailCheck);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 비밀번호 변경 페이지 인증")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/password/email")
    public Callable<Response<BaseResponse>> verifyEmailAndCodeAndUserTypeForPasswordChange(@RequestBody @Validated VerifyEmailAndCodeAndUserTypeForPasswordChange verifyEmailAndCodeAndUserTypeForPasswordChange) {
        customerVerifyService.verifyEmailAndCodeAndUserTypeForPasswordChange(verifyEmailAndCodeAndUserTypeForPasswordChange);
        return Response::new;
    }

    @Operation(summary = "고객 휴대폰 회원가입시 인증번호")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/phone/edit")
    public Callable<Response<BaseResponse>> editByPhoneSend(@RequestBody @Validated final CustomerPhoneVerifyEditRequest customerPhoneVerifyEditRequest) {
        customerVerifyService.editByPhoneSend(customerPhoneVerifyEditRequest);
        return Response::new;
    }

    @Operation(summary = "고객 이메일 회원가입시 인증번호")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PublicAccess
    @PostMapping("/email/edit")
    public Callable<Response<BaseResponse>> editByEmailSend(@RequestBody @Validated final CustomerEmailVerifyEditRequest customerEmailVerifyEditRequest){
        customerVerifyService.editByEmailSend(customerEmailVerifyEditRequest);
        return Response::new;
    }
}
