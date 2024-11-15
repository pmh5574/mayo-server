package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.List;

public class MyPageRequest {

    public record ChefInfoRequest (

            @NotNull(message = "name is not null")
            @NotEmpty(message = "name is not empty")
            String name,

            @NotNull(message = "birthday is not null")
            @NotEmpty(message = "birthday is not empty")
            @Pattern(
                    regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                    message = "생년월일은 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
            )
            String birthday,

            String email,

            String emailAuthNum,

            String phone,

            String phoneAuthNum
    ) {}

    public record ProfileRequest (

            String experience,

            String comment,

            List<String> hashtags,

            String activeRegion,

            String description,

            String additionalInfo,

            Long hopePay,

            Integer minServiceTime,

            List<String> serviceList,

            List<MappingImage> portfolio,

            List<MappingImage> license
    ) {

        @Getter
        public static class MappingImage {

            public Integer id;
            public String key;
        }

    }

//    {
//        "status": "OK",
//            "result": {
//        "portFolioUrls": [
//        {
//            "id": 1,
//                "url": "https://mayo-ap-northeast-2-s3.s3.ap-northeast-2.amazonaws.com/621721778362554936/portfolio/1_your_portfolio_description_1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240920T084542Z&X-Amz-SignedHeaders=host&X-Amz-Expires=299&X-Amz-Credential=AKIASFUIRL3OGHOTAEJN%2F20240920%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=c5b51dba5d1a8db120c843a450f990e164ad4e8da65b560374b35b57155e8872"
//        }
//        ],
//        "licenseUrls": [
//        {
//            "id": 1,
//                "url": "https://mayo-ap-northeast-2-s3.s3.ap-northeast-2.amazonaws.com/621721778362554936/license/1_your_license_description_1?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240920T084542Z&X-Amz-SignedHeaders=host&X-Amz-Expires=299&X-Amz-Credential=AKIASFUIRL3OGHOTAEJN%2F20240920%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=173b91954bc9355642d849ee1f4e8fa875a660dd1d75983be205342cc0311221"
//        },
//        {
//            "id": 2,
//                "url": "https://mayo-ap-northeast-2-s3.s3.ap-northeast-2.amazonaws.com/621721778362554936/license/2_your_license_description_2?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240920T084542Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=AKIASFUIRL3OGHOTAEJN%2F20240920%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=c398c2d4c08420ab8458139652ee7f92a843609d434894a8f827dd38bb1b99fd"
//        }
//        ]
//    }
//    }
}
