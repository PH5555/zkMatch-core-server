package com.zkrypto.zkMatch.global.utils;

import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {
    private static final DateTimeFormatter dateToStringformatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    private static final DateTimeFormatter[] StringToDateformatters = {
            DateTimeFormatter.ofPattern("yyyy.MM.dd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    public static String format(LocalDateTime dateTime) {
        return dateToStringformatter.format(dateTime);
    }

    public static LocalDate format(String date) {
        for (DateTimeFormatter formatter : StringToDateformatters) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // 실패하면 다음 포맷 시도
            }
        }
        throw new CustomException(ErrorCode.INVALID_DATE_TYPE);
    }
}
