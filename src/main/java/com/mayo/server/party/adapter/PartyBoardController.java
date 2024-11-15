package com.mayo.server.party.adapter;

import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.common.utility.CommonUtility;
import com.mayo.server.party.adapter.in.web.HomePartyListRequest;
import com.mayo.server.party.adapter.in.web.HomePartyRequest;
import com.mayo.server.party.adapter.out.persistence.PartyBoardResponse;
import com.mayo.server.party.app.port.in.PartyUseCase;
import com.mayo.server.party.app.port.out.PartyBoardUseCase;
import com.mayo.server.common.PaginationDto;
import com.mayo.server.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

@Tag(name = "Chef 홈 파티 게시판(Board)", description = "Chef Board API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/party/chef/board")
public class PartyBoardController {

    private final PartyBoardUseCase partyBoardUseCase;
    private final PartyUseCase partyUseCase;

    @Operation(summary = "요리사 홈 파티 신청")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PostMapping ("/{partyId}")
    public Callable<Response<Object>> postHomePartyApply (
            @Valid @PathVariable Long partyId,
            HttpServletRequest request
    ) {

        partyUseCase.apply(CommonUtility.getId(request.getAttribute("id")), partyId);

        return Response::new;
    }

    @Operation(summary = "요리사 홈 파티 리스트(Deprecated) 미 사용 기능")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyBoardResponse.HomePartyPage.class))})
    @PostMapping ("/party")
    public Callable<Response<PartyBoardResponse.HomePartyPage>> getHomeParties (
            @Parameter(name = "page", required = true) @Valid() @RequestParam() Integer page,
            @Parameter(name = "pageSize", required = true) @Valid() @RequestParam() Integer pageSize,
            @Parameter(name = "sort", required = true) @Valid @RequestParam() String sort,
            @Valid @RequestBody HomePartyRequest req
            ) {

        PartyBoardResponse.HomePartyPage homeParties = partyBoardUseCase.homeParty(
                new PaginationDto(page, pageSize, sort), req
        );

        Response<PartyBoardResponse.HomePartyPage> res = new Response<>();
        res.setResult(homeParties);

        return () -> res;
    }

    @Operation(summary = "요리사 홈 파티 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyBoardResponse.HomePartyPage.class))})
    @PostMapping ("/party/list")
    public Callable<Response<PartyBoardResponse.HomePartyPage>> getHomePartyList (
            @Parameter(name = "page", required = true) @Valid() @RequestParam() Integer page,
            @Parameter(name = "pageSize", required = true) @Valid() @RequestParam() Integer pageSize,
            @Parameter(name = "sort", required = true) @Valid @RequestParam() String sort,
            @Valid @RequestBody HomePartyListRequest req,
            HttpServletRequest request
    ) {

        PartyBoardResponse.HomePartyPage homeParties = partyBoardUseCase.homeParty(
                new PaginationDto(page, pageSize, sort), req, CommonUtility.getId(request.getAttribute("id"))
        );

        Response<PartyBoardResponse.HomePartyPage> res = new Response<>();
        res.setResult(Objects.isNull(homeParties) ? new PartyBoardResponse.HomePartyPage(
                List.of(),
                0L
        ) : homeParties);

        return () -> res;
    }

    @Operation(summary = "요리사 홈 파티 리스트 상세")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = PartyBoardResponse.HomePartyDetails.class))})
    @GetMapping ("/party/{partyId}")
    public Callable<Response<PartyBoardResponse.HomePartyDetails>> getHomePartyDetails(
            @PathVariable @Valid Long partyId
    ) {

        PartyBoardResponse.HomePartyDetails homeParty = partyBoardUseCase.homePartyDetails(partyId);

        Response<PartyBoardResponse.HomePartyDetails> res = new Response<>();
        res.setResult(homeParty);

        return () -> res;
    }

}
