package com.mayo.server.account.adapter;

import com.mayo.server.account.adapter.in.web.AccountRequest;
import com.mayo.server.account.adapter.out.persistence.AccountResponse;
import com.mayo.server.account.app.port.in.AccountCommandUseCase;
import com.mayo.server.account.app.port.out.AccountQueryUseCase;
import com.mayo.server.account.domain.models.ChefAccount;
import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.common.utility.CommonUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

@Tag(name = "Chef 계좌 저장", description = "Chef Account API")
@RequiredArgsConstructor
@RequestMapping("/chef/{chefId}/account")
@RestController
public class AccountController {

    private final AccountCommandUseCase accountCommandUseCase;
    private final AccountQueryUseCase accountQueryUseCase;

    @Operation(summary = "은행 입력")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/bank")
    public Callable<Response<BaseResponse>> postBank (
            @RequestBody @Valid AccountRequest.BankRequest req,
            HttpServletRequest request
    ) {

        accountCommandUseCase.bank(CommonUtility.getId(request.getAttribute("id")), req);

        return Response::new;
    }

    @Operation(summary = "계좌 저장")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/save")
    public Callable<Response<BaseResponse>> postAccount (
            @RequestBody @Valid AccountRequest.AccountRegisterRequest req,
            HttpServletRequest request
    ) {

        accountCommandUseCase.account(CommonUtility.getId(request.getAttribute("id")), req);

        return Response::new;
    }

    @Operation(summary = "계좌 정보 불러오기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = AccountResponse.class))})
    @GetMapping("")
    public Callable<Response<AccountResponse>> getAccount (
            HttpServletRequest request
    ) {

        ChefAccount chefAccount = accountQueryUseCase.getAccount(CommonUtility.getId(request.getAttribute("id")));
        Response<AccountResponse> res = new Response<>();
        res.setResult(AccountResponse.getAccount(chefAccount));

        return () -> res;
    }

}
