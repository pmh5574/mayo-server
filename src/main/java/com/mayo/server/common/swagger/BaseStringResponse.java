package com.mayo.server.common.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "BaseStringResponse")
public class BaseStringResponse {

    @Schema(description = "statusCode", example = "OK") int status;

    String result;

}
