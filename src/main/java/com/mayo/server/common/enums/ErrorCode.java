package com.mayo.server.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CHEF_DUPLICATED_USERNAME("CHEF_DUPLICATED_USERNAME", "이미 존재하는 아이디입니다."),
    CHEF_DUPLICATED_PHONE("CHEF_DUPLICATED_PHONE", "이미 존재하는 전화번호입니다."),
    CHEF_DUPLICATED_EMAIL("CHEF_DUPLICATED_EMAIL", "이미 존재하는 이메일입니다."),
    CHEF_NOT_FOUND( "CHEF_NOT_FOUND", "유저(요리사)를 찾을 수 없습니다."),
    CHEF_NOT_APPROVED( "CHEF_NOT_APPROVED", "승인된 계정이 아닙니다."),
    CHEF_PHONE_NOT_FOUND( "CHEF_PHONE_NOT_FOUND", "유저(요리사) 전화번호 인증 정보를 찾을 수 없습니다."),
    CHEF_EMAIL_NOT_FOUND( "CHEF_EMAIL_NOT_FOUND", "유저(요리사) 이메일 인증 정보를 찾을 수 없습니다."),
    CHEF_PHONE_USERNAME_NOT_FOUND( "CHEF_PHONE_USERNAME_NOT_FOUND", "유저(요리사) 아이디 찾기 인증이 발송되지 않았습니다."),
    CHEF_EMAIL_USERNAME_NOT_FOUND( "CHEF_EMAIL_USERNAME_NOT_FOUND", "유저(요리사) 아이디 찾기 인증이 발송되지 않았습니다."),

    CHEF_PARTY_IS_EXPIRED("CHEF_PARTY_IS_EXPIRED", "이미 종료된 홈파티입니다."),
    CHEF_PARTY_IS_MATCHED("CHEF_PARTY_IS_MATCHED", "이미 매칭된 홈파티입니다."),

    CHEF_BANK_NOT_FOUND("CHEF_BANK_NOT_FOUND", "은행 정보를 찾을 수 없습니다."),
    CHEF_BANK_NOT_REGISTER("CHEF_BANK_NOT_REGISTER", "은행을 먼저 선택해 주세요."),

    PAYMENT_DRAFT_NOT_FOUND("PAYMENT_DRAFT_NOT_FOUND", "결제 임시저장 정보를 찾을 수 없습니다."),
    PAYMENT_CHECKSUM_NOT_FOUND("PAYMENT_CHECKSUM_NOT_FOUND", "결제 체크섬 정보를 찾을 수 없습니다."),
    PAYMENT_CHECKSUM_ERROR("PAYMENT_CHECKSUM_NOT_FOUND", "결제 체크섬에 실패 했습니다."),

    CUSTOMER_DUPLICATED_PHONE("CUSTOMER_DUPLICATED_PHONE", "이미 존재하는 전화번호입니다."),
    CUSTOMER_DUPLICATED_USER_ID("CUSTOMER_DUPLICATED_USER_ID", "이미 존재하는 아이디입니다."),
    CUSTOMER_DUPLICATED_EMAIL("CUSTOMER_DUPLICATED_EMAIL", "이미 존재하는 이메일입니다."),
    CUSTOMER_VERIFY_USERNAME_PHONE_NOT_FOUND("CUSTOMER_VERIFY_USERNAME_PHONE_NOT_FOUND", "유저 휴대폰 인증이 발송되지 않았습니다."),
    CUSTOMER_VERIFY_USERNAME_EMAIL_NOT_FOUND("CUSTOMER_VERIFY_USERNAME_EMAIL_NOT_FOUND", "유저 이메일 인증이 발송되지 않았습니다."),
    CUSTOMER_PHONE_NOT_FOUND( "CUSTOMER_PHONE_NOT_FOUND", "유저 전화번호 인증 정보를 찾을 수 없습니다."),
    CUSTOMER_EMAIL_NOT_FOUND( "CUSTOMER_EMAIL_NOT_FOUND", "유저 이메일 인증 정보를 찾을 수 없습니다."),
    CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND", "존재하지 않는 고객입니다."),
    CUSTOMER_HOME_PARTY_NOT_FOUND("CUSTOMER_HOME_PARTY_NOT_FOUND", "파티를 찾을 수 없습니다."),
    CUSTOMER_EDIT_INVALID_INPUT("CUSTOMER_EDIT_INVALID_INPUT", "프로필 수정 시 연락처 또는 이메일 중 하나는 필수 입력 항목입니다."),

    CUSTOMER_ACCOUNT_NOT_FOUND("CUSTOMER_ACCOUNT_NOT_FOUND", "존재하지 않는 고객 계좌입니다."),
    CUSTOMER_ACCOUNT_DUPLICATED("CUSTOMER_ACCOUNT_DUPLICATED", "이미 등록된 계좌가 존재합니다."),

    PWD_NOT_EQUALS( "CHEF_NOT_FOUND", "비밀번호가 일치하지 않습니다."),
    PHONE_AUTH_NUMBER_NOT_EQUALS( "PHONE_AUTH_NUMBER_NOT_EQUALS", "전화번호 인증번호가 일치하지 않습니다."),
    EMAIL_AUTH_NUMBER_NOT_EQUALS( "EMAIL_AUTH_NUMBER_NOT_EQUALS", "이메일 인증번호가 일치하지 않습니다."),
    INVALID_PHONE_NAME_NOT_EQUALS("INVALID_PHONE_NAME_REQUEST_ERROR", "이름이 일치하지 않습니다."),
    INVALID_BIRTHDAY_NOT_EQUALS("INVALID_BIRTHDAY_NOT_EQUALS", "생년월일이 일치하지 않습니다."),
    INVALID_EMAIL_NAME_NOT_EQUALS("INVALID_EMAIL_NAME_REQUEST_ERROR", "이름과 이메일이 일치하지 않습니다."),

    CUSTOMER_VALID_USER_ID("CUSTOMER_VALID_USER_ID", "아이디는 4~20자리의 영문자와 숫자 조합이어야 합니다."),
    CUSTOMER_VALID_PASSWORD("CUSTOMER_VALID_PASSWORD", "비밀번호는 8~16자리의 영문 소문자와 숫자의 조합이어야 합니다."),
    CUSTOMER_VALID_BIRTHDAY("CUSTOMER_VALID_BIRTHDAY", "생년월일은 YYYYMMDD 형식이어야 하며, 유효한 날짜여야 합니다."),
    CUSTOMER_VALID_PHONE("CUSTOMER_VALID_PHONE", "휴대폰 번호는 하이픈이 없어야 하며, 10자리 또는 11자리 숫자여야 합니다."),
    CUSTOMER_VALID_EMAIL("CUSTOMER_VALID_EMAIL", "유효한 이메일 주소를 입력해 주세요."),

    VERIFY_EXPIRED("VERIFY_EXPIRED", "인증번호가 만료 됐습니다."),
    JWT_NOT_FOUND_TOKEN("JWT_NOT_FOUND_TOKEN", "인증정보를 찾을 수 없습니다."),
    JWT_VALIDATE_ERROR("JWT_VALIDATE_ERROR", "유효한 인증정보가 아닙니다."),

    INVALID_REQUEST_ERROR("INVALID_REQUEST_ERROR", ""),
    CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR("CUSTOMER_PASSWORD_CHECK_INVALID_REQUEST_ERROR", "잘못된 페이지 접근 입니다."),
    CUSTOMER_KITCHEN_NOT_EQUALS("CUSTOMER_KITCHEN_NOT_EQUALS", "잘못된 페이지 접근 입니다."),

    AWS_SNS_EXCEPTION("AWS_SNS_ERROR", "SMS 발송에 실패 했습니다."),
    AWS_SES_EXCEPTION("AWS_SES_ERROR", "이메일 발송에 실패 했습니다."),
    INTERNAL_SERVER_ERROR( "INTERNAL_SERVER_ERROR", ""),

    KITCHEN_NOT_FOUND("KITCHEN_NOT_FOUND", "존재하지 않는 주방 정보입니다."),

    PARTY_NOT_FOUND("PARTY_NOT_FOUND", "존재하지 않는 홈파티 정보입니다."),

    PARTY_SCHEDULE_NOT_FOUND("PARTY_SCHEDULE_NOT_FOUND", "존재하지 않는 신청 셰프 정보입니다."),
    PARTY_SCHEDULE_IS_MATCHED("PARTY_SCHEDULE_IS_MATCHED", "이미 매칭된 파티 입니다."),

    REVIEW_DUPLICATED("REVIEW_DUPLICATED", "이미 존재하는 홈파티 리뷰 입니다."),
    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND", "아직 리뷰가 작성되지 않았습니다.");

    private final String code;
    private final String message;

}
