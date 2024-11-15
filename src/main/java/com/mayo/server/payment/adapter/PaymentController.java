package com.mayo.server.payment.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.payment.adapter.in.web.PayCheckSumRequest;
import com.mayo.server.payment.adapter.in.web.PayDraftRequest;
import com.mayo.server.payment.adapter.in.web.PayRequest;
import com.mayo.server.payment.app.port.in.PayCommandUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pay")
@RestController
public class PaymentController {

    private final PayCommandUseCase payCommandUseCase;

    @Operation(summary = "결제 임시 저장")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/draft")
    public Callable<Response<BaseResponse>> postDraft(
            @RequestBody @Valid PayDraftRequest req
    ) {

        payCommandUseCase.handle(req);

        log.info("Create Payment Draft Aggregate Id :" + req.orderId());

        return Response::new;
    }

    @Operation(summary = "결제 체크섬 검증")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/check")
    public Callable<Response<BaseResponse>> postCheckSum(
            @RequestBody @Valid PayCheckSumRequest req
    ) {

        payCommandUseCase.handle(req);

        log.info("Create Payment CheckSum Aggregate Id :" + req.orderId());

        return Response::new;
    }

    @Operation(summary = "결제 최종 승인")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("")
    public Callable<Response<BaseResponse>> postPayment(
            @RequestBody @Valid PayRequest req
    ) {

        payCommandUseCase.handle(req);

        log.info("Create Payment Aggregate Id :" + req.orderId());

        return Response::new;
    }

}
