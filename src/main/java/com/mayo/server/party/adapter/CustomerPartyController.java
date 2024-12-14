package com.mayo.server.party.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.party.adapter.in.web.CustomerPartyChefRegisterRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyFinishSearchRequest;
import com.mayo.server.party.adapter.in.web.CustomerPartyRegisterRequest;
import com.mayo.server.party.adapter.out.persistence.HomePartyFinishListResponse;
import com.mayo.server.party.adapter.out.persistence.HomePartyStatusResponse;
import com.mayo.server.party.app.port.out.ChefNotSelectedDto;
import com.mayo.server.party.app.port.out.HomePartyDetail;
import com.mayo.server.party.app.port.out.HomePartyNoReviewFinishListDto;
import com.mayo.server.party.app.port.out.HomePartyRegisterKitchenListDto;
import com.mayo.server.party.app.service.CustomerPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/customer/party")
@RestController
public class CustomerPartyController {

    private final CustomerPartyService customerPartyService;

    @Operation(summary = "고객 셰프에게 홈파티 신청 및 등록")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("/chef")
    public Callable<Response<BaseResponse>> postPartyToChef(
            @RequestBody @Validated final CustomerPartyChefRegisterRequest customerPartyChefRegisterRequest,
            @RequestHeader("Authorization") final String token) {
        customerPartyService.savePartyToChef(customerPartyChefRegisterRequest, token);
        return Response::new;
    }

    @Operation(summary = "고객 홈파티 등록")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping("")
    public Callable<Response<BaseResponse>> postParty(
            @RequestBody @Validated final CustomerPartyRegisterRequest customerPartyRegisterRequest,
            @RequestHeader("Authorization") final String token) {
        customerPartyService.saveParty(customerPartyRegisterRequest, token);
        return Response::new;
    }

    @Operation(summary = "고객 홈파티 매칭 내역")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("")
    public Callable<Response<List<HomePartyStatusResponse>>> getParty(
            @RequestHeader("Authorization") final String token) {

        return () -> new Response<>(customerPartyService.getPartyList(token));
    }

    @Operation(summary = "고객 홈파티 상세")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = HomePartyDetail.class))})
    @GetMapping("/{homePartyId}")
    public Callable<Response<HomePartyDetail>> getPartyDetail(
            @RequestHeader("Authorization") final String token, @PathVariable final String homePartyId) {
        return () -> new Response<>(customerPartyService.getPartyDetail(homePartyId, token));
    }

    @Operation(summary = "고객 홈파티 이용 완료 내역")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = HomePartyFinishListResponse.class))})
    @GetMapping("/finish/list")
    public Callable<Response<HomePartyFinishListResponse>> getFinishParty(
            @ModelAttribute @Validated final CustomerPartyFinishSearchRequest customerPartyFinishSearchRequest,
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(customerPartyService.getFinishPartyList(customerPartyFinishSearchRequest, token));
    }

    @Operation(summary = "고객 홈파티 리뷰 없는 이용 완료 내역")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("/finish/no-review/list")
    public Callable<Response<List<HomePartyNoReviewFinishListDto>>> getFinishPartyNoReview(
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(customerPartyService.getFinishPartyNoReviewList(token));
    }

    @Operation(summary = "홈파티 등록시 주방 조회")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("/kitchen/list")
    public Callable<Response<List<HomePartyRegisterKitchenListDto>>> getKitchenList(
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(customerPartyService.getKitchenList(token));
    }

    @Operation(summary = "홈파티에 신청한 마이요리사 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("/chef/list/{partyId}")
    public Callable<Response<List<ChefNotSelectedDto>>> getChefList(
            @PathVariable final Long partyId,
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(customerPartyService.getChefNotSelectedList(partyId, token));
    }

    @Operation(summary = "홈파티에 신청한 마이요리사 선정")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @PostMapping("/chef/assign/{partyScheduleId}")
    public Callable<Response<List<ChefNotSelectedDto>>> postAssignChef(
            @PathVariable final Long partyScheduleId,
            @RequestHeader("Authorization") final String token) {
        customerPartyService.postAssignChef(partyScheduleId, token);
        return Response::new;
    }
}
