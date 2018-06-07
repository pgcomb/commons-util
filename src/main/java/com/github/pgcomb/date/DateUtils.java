package com.github.pgcomb.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 王东旭
 */
public class DateUtils {

    public static String date2Str(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ISO_DATE);
    }

    public static String date2Str(){
        return date2Str(LocalDate.now());
    }

    public static LocalDate str2Date(String date){
        return LocalDate.parse(date,DateTimeFormatter.ISO_DATE);
    }

    public static int[] date2Array(String date){
        return date2Array(str2Date(date));
    }

    public static int[] date2Array(LocalDate date){
        return new int[]{date.getYear(),date.getMonth().getValue(),date.getDayOfMonth()};
    }

    public static int[] date2Array(){
        return date2Array(LocalDate.now());
    }

    public static String time2Str(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String time2Str(){
        return time2Str(LocalDateTime.now());
    }

    public static LocalDateTime str2Time(String date){
        return LocalDateTime.parse(date,DateTimeFormatter.ISO_DATE_TIME);
    }

    public static int[] time2Array(String date){
        return time2Array(str2Time(date));
    }

    public static int[] time2Array(LocalDateTime date){
        return new int[]{date.getYear(),date.getMonth().getValue(),date.getDayOfMonth(),
                date.getHour(),date.getMinute(),date.getSecond(),date.getNano()};
    }
    public static int[] time2Array(){
        return time2Array(LocalDateTime.now());
    }
}
