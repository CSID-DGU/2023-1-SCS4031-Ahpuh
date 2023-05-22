package com.example.ahpuh.util;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    EMPTY_TOKEN(false, 1001, "토큰을 확인해주세요."),
    DUPLICATE_NICKNAME(false, 2000, "닉네임이 중복되었습니다."),
    DUPLICATE_EMAIL(false, 2001, "이메일이 중복되었습니다."),
    EMPTY_JWT(false, 2002, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2003, "유효하지 않은 JWT입니다."),
    POST_ADMIN_EMPTY_EMAIL(false, 2004, "이메일을 입력해주세요."),
    POST_ADMIN_EMPTY_PASSWORD(false, 2005, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2006, "비밀번호가 틀렸습니다."),
    FAILED_TO_LOGIN(false, 2007, "없는 아이디이거나 비밀번호가 틀렸습니다."),
    POST_ADMIN_EMPTY(false, 2008, "이메일 또는 비밀번호를 입력하지 않았습니다."),
    POST_ADMIN_INVALID_EMAIL(false, 2009, "유효하지 않은 이메일 형식입니다."),
    NOT_FIND_ADMIN(false, 2010, "해당 관리자 정보를 찾을 수 없습니다."),
    NOT_FIND_CCTV(false, 2011, "CCTV 정보를 찾을 수 없습니다."),
    INVALID_POS_NUM(false, 2012, "라인 개수와 맞는 좌표값을 입력해주세요."),
    EXIST_POSITION(false, 2023, "기존에 좌표를 선정하였습니다."),

    /*
     * 4000: [POST]
     * */
    PASSWORD_ENCRYPTION_ERROR(false, 4001, "비밀번호 암호화에 실패했습니다."),
    DATABASE_ERROR(false, 4002, "데이터베이스 연결에 실패하였습니다."),
    IMAGE_UPLOAD_NONE(false, 4003, "업로드할 이미지가 없습니다."),
    WRONG_IMAGE_FORMAT(false, 4004, "옳지 않은 이미지 형식입니다."),

    /*
     * 5000: database error
     * */
    IMAGE_UPLOAD_ERROR(false, 5001, "이미지 업로드 에러가 발생하였습니다."),
    /*
     * 7000 : PATCH
     * */

    /*
     * 8000 : delete
     * */

    /*
     * 9500 : jwt
     * */

    WRONG_JWT_SIGN_TOKEN(false, 9500, "잘못된 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(false, 9501, "만료된 JWT 토큰 입니다."),
    UNSUPPORTED_JWT_TOKEN(false, 9502, "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(false, 9503, "JWT 토큰이 잘못되었습니다."),
    NULL_JWT(false,9504, "JWT의 값이 없습니다."),
    INVALID_JWT_TOKEN(false, 9505, "Refresh Token 이 유효하지 않습니다."),
    NOT_SAME_USER_INFO(false, 9506, "토큰의 유저 정보가 일치하지 않습니다."),
    LOGOUT_USER(false, 9507, "로그아웃된 사용자입니다.")
    ;


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
