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
    INVALID_EMAIL_AUTH("A003", HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다."),
    NOT_FOUND_AUTH_NUMBER("A004", HttpStatus.BAD_REQUEST, "인증 번호가 존재하지 않습니다."),

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
    NOT_EVALUATE_TARGET("R002", HttpStatus.CONFLICT, "평가 대상이 아닙니다."),
    INVALID_VC_FORMAT("R003", HttpStatus.CONFLICT, "유효하지 않은 VC 포맷입니다."),
    INVALID_VC_TYPE("R004", HttpStatus.CONFLICT, "유효하지 않은 VC 타입입니다."),
    NOT_FOUND_RESUME("R005", HttpStatus.NOT_FOUND, "해당 이력이 존재하지 않습니다."),
    NOT_ALLOWED_DELETE_RESUME("R006", HttpStatus.FORBIDDEN, "이력을 삭제할 권한이 없습니다."),
    NOT_FOUND_APPLY_SESSION("R007", HttpStatus.NOT_FOUND, "지원 세션을 찾을 수 없습니다."),
    INVALID_APPLY_CONDITION("R008", HttpStatus.CONFLICT, "지원 조건을 만족하지 않습니다."),
    NOT_FOUNT_POLICY("R009", HttpStatus.NOT_FOUND, "해당 vp policy를 찾을 수 없습니다."),
    EXPIRED_APPLICATION("R010", HttpStatus.CONFLICT, "지원 조건 확인 유효시간이 지났습니다."),

    NOT_FOUND_INTERVIEW("I001", HttpStatus.NOT_FOUND, "면접 일정이 존재하지 않습니다."),
    NOT_INTERVIEW_TARGET("I002", HttpStatus.CONFLICT, "면접 대상이 아닙니다."),

    FAILED_ENC("C001", HttpStatus.BAD_REQUEST, "암호화에 실패했습니다."),
    FAILED_DEC("C002", HttpStatus.BAD_REQUEST, "복호화에 실패했습니다."),
    FAILED_DERIVEKEY("C003", HttpStatus.BAD_REQUEST, "키복구에 실패했습니다."),

    FAILED_UPLOAD_FILE("F001", HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),

    VP_OFFER_NOT_FOUND("V001", HttpStatus.NOT_FOUND, "vp offer를 찾을 수 없습니다."),
    VP_JSON_FORMAT_ERROR("V002", HttpStatus.BAD_REQUEST, "vp json 형식이 잘못됐습니다."),
    VP_QR_MAKE_ERROR("V003", HttpStatus.BAD_REQUEST, "QR 생성에 실패했습니다."),
    NOT_FOUND_OFFER_ID("V004", HttpStatus.NOT_FOUND, "offer id가 존재하지 않습니다."),
    FAILED_GET_VP("V005", HttpStatus.NOT_FOUND, "VP 생성에 실패했습니다."),
    NOT_FOUND_VP_TYPE("V006", HttpStatus.NOT_FOUND, "VP TYPE을 찾을 수 없습니다."),

    NOT_FOUND_ISSUER_KEY("K001", HttpStatus.NOT_FOUND, "ISSUER KEY를 찾을 수 없습니다."),

    NOT_FOUND_APPLICATION("A001", HttpStatus.NOT_FOUND, "지원 데이터를 찾을 수 없습니다."),
    NOT_FOUND_VK("A002", HttpStatus.NOT_FOUND, "VK를 찾을 수 없습니다."),

    INVALID_DATE_TYPE("D001", HttpStatus.BAD_REQUEST, "VC date 타입이 잘못됐습니다."),

    ;


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;
}
