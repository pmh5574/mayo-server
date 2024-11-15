package com.mayo.server.common.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "기본 응답")
public class BaseResponse {

    @Schema(description = "statusCode", example = "OK") protected int status;

    @Schema(description = "statusCode", example = "Success") protected int result;

}
