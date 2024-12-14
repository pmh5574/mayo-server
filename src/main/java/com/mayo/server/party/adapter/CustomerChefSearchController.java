package com.mayo.server.party.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.annotation.PublicAccess;
import com.mayo.server.party.adapter.in.web.CustomerPartySearchRequest;
import com.mayo.server.party.app.port.out.ChefSearch;
import com.mayo.server.party.app.service.CustomerBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "고객 게시판", description = "고객 게시판 API")
@RequiredArgsConstructor
@RequestMapping("/customer/chef/search")
@RestController
public class CustomerChefSearchController {

    private final CustomerBoardService customerBoardService;

    @Operation(summary = "고객 마이요리사 찾기")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @PublicAccess
    @GetMapping("")
    public Callable<Response<List<ChefSearch>>> getSearchChefAll(@ModelAttribute @Validated final CustomerPartySearchRequest customerPartySearchRequest) {
        return () -> new Response<>(customerBoardService.getSearchChefAll(customerPartySearchRequest));
    }
}
