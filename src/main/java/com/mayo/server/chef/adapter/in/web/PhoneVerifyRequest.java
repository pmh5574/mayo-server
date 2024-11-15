package com.mayo.server.chef.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PhoneVerifyRequest {

    public record UsernameRequest (

            @NotNull(message = "이름을 입력해 주세요")
            @NotBlank(message = "이름를 입력해 주세요")
            String name,
            @NotNull(message = "전화번호를 입력해 주세요")
            @NotBlank(message = "전화번호를 입력해 주세요")
            @Pattern(
                    regexp = "^(?!.*-)[0-9]{10,11}$",
                    message = "휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."
            )
            String phone,

            @NotNull(message = "인증번호를 입력해 주세요")
            @NotBlank(message = "인증번호를 입력해 주세요")
            String authNum
    ) {
    }

    public record PwdRequest (

            @NotNull(message = "아이디를 입력해 주세요")
            @NotBlank(message = "아이디를 입력해 주세요")
            @Pattern(
                    regexp = "^[a-zA-Z\\d]{4,20}$",
                    message = "아이디는 4~20자리의 영문자와 숫자 조합이어야 합니다."
            )
            String username,

            @NotNull(message = "이름를 입력해 주세요")
            @NotBlank(message = "이름를 입력해 주세요")
            String name,

            @NotNull(message = "생년월일을 입력해 주세요")
            @NotBlank(message = "생년월일을 입력해 주세요")
            @Pattern(
                    regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$",
                    message = "생년월일은 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."
            )
            String birthday,

            @NotNull(message = "전화번호를 입력해 주세요")
            @NotBlank(message = "전화번호를 입력해 주세요")
            @Pattern(
                    regexp = "^(?!.*-)[0-9]{10,11}$",
                    message = "휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."
            )
            String phone,

            @NotNull(message = "인증번호 입력해 주세요")
            @NotBlank(message = "인증번호를 입력해 주세요")
            String authNum
    ) {
    }

}
