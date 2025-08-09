package com.zkrypto.zkMatch.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),

    NOT_FOUND_MEMBER_ID("A001", HttpStatus.NOT_FOUND, "아이디가 존재하지 않습니다."),
    INVALID_MEMBER_PASSWORD("A002", HttpStatus.BAD_REQUEST, "비밀번호가 불일치합니다."),

    NOT_FOUND_MEMBER("M001", HttpStatus.NOT_FOUND, "멤버가 존재하지 않습니다."),
    ID_DUPLICATION("M002", HttpStatus.BAD_REQUEST, "중복되는 아이디입니다."),

    CORPORATION_DUPLICATION("C001", HttpStatus.BAD_REQUEST, "이미 존재하는 법인입니다."),

    NOT_FOUND_POST("P001", HttpStatus.NOT_FOUND, "공고가 존재하지 않습니다."),
    ALREADY_APPLIED_POST("P002", HttpStatus.CONFLICT, "이미 지원한 공고입니다."),
    EXPIRED_POST("P003", HttpStatus.BAD_REQUEST, "마감된 공고입니다."),
    NOT_APPLIED_TO_POSTING("P004", HttpStatus.BAD_REQUEST, "해당 공고에 지원한 이력이 없는 멤버입니다."),
    ALREADY_PASSED("P005", HttpStatus.CONFLICT, "이미 합격한 지원자입니다."),
    ALREADY_FAILED("P006", HttpStatus.CONFLICT, "이미 탈락한 지원자입니다."),
    NOT_PERMITTED_STATUS("P007", HttpStatus.BAD_REQUEST, "허용되지 않은 status 입니다."),

    NOT_FOUND_RECRUIT("R001", HttpStatus.NOT_FOUND, "지원 이력이 존재하지 않습니다."),
    ALREADY_HAS_INTERVIEW("R001", HttpStatus.CONFLICT, "이미 면접 일정이 존재합니다.");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;
}
