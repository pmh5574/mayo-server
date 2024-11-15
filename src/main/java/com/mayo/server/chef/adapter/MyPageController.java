package com.mayo.server.chef.adapter;

import com.mayo.server.chef.adapter.in.web.MyPageRequest;
import com.mayo.server.chef.adapter.out.persistence.MyPageResponse;
import com.mayo.server.chef.adapter.out.persistence.ProfileResponse;
import com.mayo.server.chef.adapter.out.persistence.ProfileUpdateResponse;
import com.mayo.server.chef.app.port.in.MyPageCommandUseCase;
import com.mayo.server.chef.app.port.out.MyPageQueryUseCase;
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

@Tag(name = "Chef My Page", description = "Chef MyPage API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chef/mypage")
public class MyPageController {

    private final MyPageCommandUseCase myPageCommandUseCase;
    private final MyPageQueryUseCase myPageQueryUseCase;

    @Operation(summary = "요리사 프로필")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = MyPageResponse.class))})
    @GetMapping("/{id}/profile")
    public Callable<Response<MyPageResponse>> getProfile(
            @PathVariable() String id
    ) {

        return () -> new Response<>(
                myPageQueryUseCase.profile(id)
        );
    }

    @Operation(summary = "요리사 프로필 업데이트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PatchMapping("")
    public Callable<Response<String>> updateMyPage(
            @Valid @RequestBody MyPageRequest.ChefInfoRequest req,
            HttpServletRequest request
    ) {

        myPageCommandUseCase.updateMyPage(
                req,
                CommonUtility.getId(request.getAttribute("id"))
        );

        return Response::new;
    }

    @Operation(summary = "요리사 활동 프로필")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = ProfileResponse.class))})
    @GetMapping("/{id}/active-profile")
    public Callable<Response<ProfileResponse>> getActiveProfile(
            @PathVariable() Long id
    ) {

        return () -> new Response<>(
                myPageQueryUseCase.activeProfile(id)
        );
    }

    @Operation(summary = "요리사 활동 프로필 업데이트")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(
            implementation = BaseResponse.class))})
    @PatchMapping("/active-profile")
    public Callable<Response<ProfileUpdateResponse>> updateProfile(
            @Valid @RequestBody MyPageRequest.ProfileRequest req,
            HttpServletRequest request
    ) {

        ProfileUpdateResponse response = myPageCommandUseCase.updateProfile(
                req,
                CommonUtility.getId(request.getAttribute("id"))
        );

        return () -> new Response<>(response);
    }



}
