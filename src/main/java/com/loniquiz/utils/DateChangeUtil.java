package com.loniquiz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateChangeUtil {
    public static String postDateChang(LocalDateTime date){
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return pattern.format(date);
    }
}
