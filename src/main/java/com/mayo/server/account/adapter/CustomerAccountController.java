package com.mayo.server.account.adapter;

import com.mayo.server.account.adapter.in.web.CustomerAccountRequest;
import com.mayo.server.account.app.service.CustomerAccountService;
import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/customer/account")
@RestController
public class CustomerAccountController {

    private final CustomerAccountService customerAccountService;

    @Operation(summary = "계좌 등록")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("")
    public Callable<Response<BaseResponse>> postAccount (
            @RequestBody @Validated CustomerAccountRequest customerAccountRequest,
            @RequestHeader("Authorization") final String token
    ) {
        customerAccountService.postAccount(customerAccountRequest, token);
        return Response::new;
    }

    @Operation(summary = "계좌 조회")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @GetMapping("")
    public Callable<Response<BaseResponse>> getAccount (
            @RequestHeader("Authorization") final String token
    ) {
        customerAccountService.getAccount(token);
        return Response::new;
    }
}
