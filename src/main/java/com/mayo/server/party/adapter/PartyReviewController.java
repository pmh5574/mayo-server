package com.mayo.server.party.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.party.adapter.in.web.PartyReviewRegisterRequest;
import com.mayo.server.party.app.port.out.PartyReviewListDto;
import com.mayo.server.party.app.service.PartyReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
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
@RequestMapping("/party/review")
@RestController
public class PartyReviewController {

    private final PartyReviewService partyReviewService;

    @Operation(summary = "후기 작성하기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("")
    public Callable<Response<BaseResponse>> postPartyReview(
            @RequestBody @Validated final PartyReviewRegisterRequest partyReviewRegisterRequest,
            @RequestHeader("Authorization") final String token) {
        partyReviewService.saveReview(partyReviewRegisterRequest, token);
        return Response::new;
    }

    @Operation(summary = "홈파티 리뷰 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("/list")
    public Callable<Response<List<PartyReviewListDto>>> getReviewList(
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(partyReviewService.getReviewList(token));
    }
}