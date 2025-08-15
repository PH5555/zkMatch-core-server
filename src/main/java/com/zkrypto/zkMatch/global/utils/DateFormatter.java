package com.zkrypto.zkMatch.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    public static String format(LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }
}
