package com.mayo.server.auth.adapter;

import com.mayo.server.auth.adapter.out.persistence.ChefInfoResponse;
import com.mayo.server.auth.adapter.out.persistence.CustomerInfoResponse;
import com.mayo.server.auth.app.port.in.AuthOutputUseCase;
import com.mayo.server.chef.domain.model.Chef;
import com.mayo.server.common.Response;
import com.mayo.server.common.utility.CommonUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthOutputUseCase outputUseCase;

    @Operation(summary = "요리사 정보")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = ChefInfoResponse.class))})
    @GetMapping("/chef/info")
    public Callable<Response<ChefInfoResponse>> getChefInfo(
            HttpServletRequest request
    ) {

        Chef chef = outputUseCase.getChefInfo(CommonUtility.getId(request.getAttribute("id")));
        Response<ChefInfoResponse> res = new Response<>();
        res.setResult(ChefInfoResponse.getChefInfo(chef));
        return () -> res;
    }

    @Operation(summary = "고객 정보")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerInfoResponse.class))})
    @PostMapping("/customer/info")
    public Callable<Response<CustomerInfoResponse>> getCustomerInfo(@RequestHeader("Authorization") final String token) {
        return () -> new Response<>(outputUseCase.getCustomerInfo(token));
    }
}
