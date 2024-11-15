package com.mayo.server.customer.adapter;

import com.mayo.server.common.Response;
import com.mayo.server.common.swagger.BaseResponse;
import com.mayo.server.customer.adapter.in.web.CustomerEdit;
import com.mayo.server.customer.adapter.in.web.KitchenEdit;
import com.mayo.server.customer.adapter.in.web.KitchenRegister;
import com.mayo.server.customer.adapter.out.persistence.CustomerImageListResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerKitchenListResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerKitchenResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerMainKitchenResponse;
import com.mayo.server.customer.adapter.out.persistence.CustomerMyPageProfile;
import com.mayo.server.customer.app.service.CustomerMyPageService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer MyPage", description = "Customer MyPage API")
@RequiredArgsConstructor
@RequestMapping("/customer/mypage")
@RestController
public class CustomerMyPageController {

    private final CustomerMyPageService myPageService;

    @Operation(summary = "고객 프로필")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerMyPageProfile.class))})
    @GetMapping("/{customerId}")
    public Callable<Response<CustomerMyPageProfile>> getProfile(@PathVariable final Long customerId, @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(myPageService.getCustomerProfile(customerId, token));
    }

    @Operation(summary = "고객 주방 등록")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerMyPageProfile.class))})
    @PostMapping("/kitchen")
    public Callable<Response<CustomerImageListResponse>> postKitchen(@RequestBody @Validated final KitchenRegister kitchenRegister, @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(myPageService.postKitchen(kitchenRegister, token));
    }

    @Operation(summary = "고객 주방 수정")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerMyPageProfile.class))})
    @PatchMapping("/kitchen/{kitchenId}")
    public Callable<Response<CustomerImageListResponse>> editKitchen(
            @RequestBody @Validated final KitchenEdit kitchenEdit,
            @RequestHeader("Authorization") final String token,
            @PathVariable final Long kitchenId) {
        return () -> new Response<>(myPageService.editKitchen(kitchenEdit, token, kitchenId));
    }

    @Operation(summary = "고객 주방")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerKitchenResponse.class))})
    @GetMapping("/kitchen/{kitchenId}")
    public Callable<Response<CustomerKitchenResponse>> getKitchen(
            @RequestHeader("Authorization") final String token,
            @PathVariable final Long kitchenId) {
        return () -> new Response<>(myPageService.getKitchen(token, kitchenId));
    }

    @Operation(summary = "고객 프로필 수정")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PatchMapping("")
    public Callable<Response<BaseResponse>> editCustomer(
            @RequestBody @Validated final CustomerEdit customerEdit,
            @RequestHeader("Authorization") final String token) {
        myPageService.editCustomer(customerEdit, token);
        return Response::new;
    }

    @Operation(summary = "주방 리스트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @GetMapping("/kitchen/list")
    public Callable<Response<List<CustomerKitchenListResponse>>> getKitchenList(
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(myPageService.getKitchenList(token));
    }

    @Operation(summary = "메인 주방 변경")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = List.class))})
    @PatchMapping("/kitchen/main/{kitchenId}")
    public Callable<Response<List<CustomerKitchenListResponse>>> patchMainKitchen(
            @PathVariable final Long kitchenId,
            @RequestHeader("Authorization") final String token) {
        myPageService.patchMainKitchen(kitchenId, token);
        return Response::new;
    }

    @Operation(summary = "메인 주방")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = CustomerMainKitchenResponse.class))})
    @GetMapping("/kitchen/main")
    public Callable<Response<CustomerMainKitchenResponse>> getMainKitchen(
            @RequestHeader("Authorization") final String token) {
        return () -> new Response<>(myPageService.getMainKitchen(token));
    }
}
