package com.mayo.server.party.adapter;

import com.mayo.server.common.PaginationWithTimeDto;
import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseNumberResponse;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.common.utility.CommonUtility;
import com.mayo.server.party.adapter.in.web.AcceptRequest;
import com.mayo.server.party.adapter.in.web.MyHomePartyListRequest;
import com.mayo.server.party.adapter.out.persistence.MyMatchedHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.MyWaitHomePartyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyApplyResponse;
import com.mayo.server.party.adapter.out.persistence.PartyReviewDto;
import com.mayo.server.party.app.port.in.AcceptUseCase;
import com.mayo.server.party.app.port.out.HomePartyDetailDto;
import com.mayo.server.party.app.port.out.PartyApplyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chef 홈 파티 예약 관리(Apply)", description = "Chef Apply API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/party/chef/apply")
public class PartyApplyController {

    private final PartyApplyUseCase partyApplyUseCase;
    private final AcceptUseCase acceptUseCase;

    @Operation(summary = "[요리사][매칭관리] 답변을 기다리는 요청들")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyList.class))})
    @GetMapping ("/wait")
    public Callable<Response<List<PartyApplyResponse.ApplyList>>> postHomePartyApply (
            HttpServletRequest request
    ) {

        List<PartyApplyResponse.ApplyList> result = partyApplyUseCase.getApplyList(CommonUtility.getId(request.getAttribute("id")));
        Response<List<PartyApplyResponse.ApplyList>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 답변을 기다리는 요청들 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> postHomePartyApply (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 답변을 기다리는 요청들 승인!!!!!")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping ("/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> postHomePartyApplyAccept (
            @Valid @RequestBody AcceptRequest req,
            @PathVariable Long customerHomePartyId,
            HttpServletRequest request
    ) {

        Long id = CommonUtility.getId(request.getAttribute("id"));

        acceptUseCase.accept(id, customerHomePartyId, req);

        return Response::new;
    }

    @Operation(summary = "[요리사][매칭관리] 매칭 대기중인 홈 파티")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyList.class))})
    @GetMapping ("/match/wait")
    public Callable<Response<List<PartyApplyResponse.MatchingWaitingList>>> getHomePartyApplyMatchingWaitingList (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int pageSize,
            @RequestParam(defaultValue = "desc") String sort,
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(page, pageSize, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<PartyApplyResponse.MatchingWaitingList> result = partyApplyUseCase.getMatchingWaitingList(id, dto);
        Response<List<PartyApplyResponse.MatchingWaitingList>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 매칭 대기중인 홈 파티 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/wait/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getHomePartyApplyMatchingWaitingListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 매칭된 홈파티")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyList.class))})
    @GetMapping ("/match/matched")
    public Callable<Response<List<PartyApplyResponse.MatchingList>>> getHomePartyApplyMatchedList (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int pageSize,
            @RequestParam(defaultValue = "desc") String sort,
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(page, pageSize, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<PartyApplyResponse.MatchingList> result = partyApplyUseCase.getMatchedList(id, dto);
        Response<List<PartyApplyResponse.MatchingList>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 매칭된 홈파티 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/matched/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getHomePartyApplyMatchedListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 방문 완료된 홈파티")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyList.class))})
    @GetMapping ("/match/finished")
    public Callable<Response<List<PartyApplyResponse.VisitList>>> getHomePartyFinishedList (
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(1,4, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<PartyApplyResponse.VisitList> result = partyApplyUseCase.getVisitList(id, dto);
        Response<List<PartyApplyResponse.VisitList>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][매칭관리] 방문 완료된 홈파티 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/finished/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getHomePartyFinishedListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 답변을 기다리는 요청들 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = MyWaitHomePartyResponse.class))})
    @PostMapping ("/match/wait/{chefId}/list")
    public Callable<Response<List<MyWaitHomePartyResponse>>> getMyWaitHomePartyList (
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request,
            @RequestBody @Valid MyHomePartyListRequest req
            ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(1,4, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<MyWaitHomePartyResponse> result = partyApplyUseCase.getMyWaitHomePartyList(id, dto, req);
        Response<List<MyWaitHomePartyResponse>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 답변을 기다리는 요청들 리스트 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/wait/{chefId}/list/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getMyWaitHomePartyListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 답변을 기다리는 요청들 리스트 페이지네이션 카운트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseNumberResponse.class))})
    @GetMapping ("/match/wait/{chefId}/list/count")
    public Callable<Response<Long>> getMyWaitHomePartyListCount (
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request,
            @RequestBody @Valid MyHomePartyListRequest req
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(1,4, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        Long result = partyApplyUseCase.getMyWaitCount(id, dto, req);
        Response<Long> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 매칭된 홈 파티 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = MyMatchedHomePartyResponse.class))})
    @PostMapping ("/match/matched/{chefId}/list")
    public Callable<Response<List<MyMatchedHomePartyResponse>>> getMyMatchedHomePartyList (
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request,
            @RequestBody @Valid MyHomePartyListRequest req
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(1,4, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<MyMatchedHomePartyResponse> result = partyApplyUseCase.getMyMatchedHomePartyList(id, dto, req);
        Response<List<MyMatchedHomePartyResponse>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 매칭된 홈 파티 리스트 페이지네이션 카운트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseNumberResponse.class))})
    @GetMapping ("/match/matched/{chefId}/list/count")
    public Callable<Response<Long>> getMyMatchedHomePartyListCount (
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request,
            @RequestBody @Valid MyHomePartyListRequest req
    ) {
        PaginationWithTimeDto dto = new PaginationWithTimeDto(1,4, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        Long result = partyApplyUseCase.getMyMatchedCount(id, dto, req);
        Response<Long> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 매칭된 홈 파티 리스트 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/matched/{chefId}/list/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getMyMatchedHomePartyListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 방문 완료된 홈 파티 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = MyMatchedHomePartyResponse.class))})
    @PostMapping ("/match/finished/{chefId}/list")
    public Callable<Response<List<MyMatchedHomePartyResponse>>> getMyFinishedHomePartyList (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "desc") String sort,
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(page, pageSize, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        List<MyMatchedHomePartyResponse> result = partyApplyUseCase.getMyFinishedHomePartyList(id, dto);
        Response<List<MyMatchedHomePartyResponse>> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "나의 [홈 파티 일정]완료된 홈 파티 리스트 페이지네이션 카운트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = Long.class))})
    @GetMapping ("/match/finished/{chefId}/list/count")
    public Callable<Response<Long>> getMyFinishedHomePartyListCount (
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int pageSize,
            @RequestParam(defaultValue = "desc") String sort,
            @Nullable @RequestParam(required = false, defaultValue = "2001-01-01 00:00:00") @Parameter(example = "2001-01-01T00:00:00") String startAt,
            @Nullable @RequestParam(required = false, defaultValue = "2029-12-30 23:59:59") @Parameter(example = "2001-01-01T00:00:00") String endAt,
            HttpServletRequest request
    ) {

        PaginationWithTimeDto dto = new PaginationWithTimeDto(page, pageSize, startAt, endAt);
        Long id = CommonUtility.getId(request.getAttribute("id"));

        Long result = partyApplyUseCase.getMyFinishedCount(id, dto);
        Response<Long> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

    @Operation(summary = "[요리사][나의 홈파티 일정] 방문 완료된 홈 파티 리스트 상세보기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyApplyResponse.ApplyHomaParty.class))})
    @GetMapping ("/match/finished/{chefId}/list/party/{customerHomePartyId}")
    public Callable<Response<PartyApplyResponse.ApplyHomaParty>> getMyFinishedHomePartyListDetail (
            @PathVariable Long customerHomePartyId
    ) {

        HomePartyDetailDto homePartyDetailDto = partyApplyUseCase.getCustomerHomeParty(customerHomePartyId);

        Response<PartyApplyResponse.ApplyHomaParty> response = new Response<>();
        response.setResult(new PartyApplyResponse.ApplyHomaParty(
                homePartyDetailDto.getCustomerHomeParty().getCustomerHomePartyId(),
                homePartyDetailDto.getCustomerHomeParty().getPartyInfo(),
                homePartyDetailDto.getAddress(),
                homePartyDetailDto.getCustomerHomeParty().getPartyComment(),
                homePartyDetailDto.getCustomerHomeParty().getBudget(),
                homePartyDetailDto.getCustomerHomeParty().getPartySchedule(),
                homePartyDetailDto.getCustomerHomeParty().getNumberOfPeople(),
                homePartyDetailDto.getCustomerHomeParty().getAdultCount(),
                homePartyDetailDto.getCustomerHomeParty().getChildCount(),
                homePartyDetailDto.getCustomerHomeParty().getServiceList(),
                homePartyDetailDto.getBurnerType(),
                homePartyDetailDto.getKitchenImages(),
                homePartyDetailDto.getKitchenTools(),
                homePartyDetailDto.getKitchenRequirements(),
                homePartyDetailDto.getKitchenConsiderations()
        ));

        return () -> response;
    }

    @Operation(summary = "나의 [홈 파티 일정]완료된 홈 파티 후기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyReviewDto.class))})
    @GetMapping ("/match/finished/{chefId}/list/review")
    public Callable<Response<PartyReviewDto>> getMyFinishedHomePartyReview (
            @RequestParam(required = true) Long customerHomePartyId
    ) {

        PartyReviewDto result = partyApplyUseCase.getHomePartyReview(customerHomePartyId);
        Response<PartyReviewDto> response = new Response<>();
        response.setResult(result);

        return () -> response;
    }

}
