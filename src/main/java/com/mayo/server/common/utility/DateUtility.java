package com.mayo.server.common.utility;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.mayo.server.common.Constants.yyyy_MM_DD_HH_mm_ss;

public class DateUtility {
    public static DateTimeFormatter dateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static String getUTC0String(String pattern){
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));

        DateTimeFormatter formatter = dateTimeFormatter(pattern);

        return utcNow.format(formatter);
    }

    public static String getUTCTransDateTime(
            String date,
            String pattern
    ) {

        DateTimeFormatter dateFormatter = dateTimeFormatter(pattern);

        LocalDateTime localDateTime = LocalDateTime.parse(
                date,
                dateFormatter
        );
        ZonedDateTime seoulZonedDateTime = localDateTime.atZone(
                ZoneId.of("Asia/Seoul")
        );
        ZonedDateTime utcZonedDateTime = seoulZonedDateTime.withZoneSameInstant(
                ZoneId.of("UTC")
        );

        return utcZonedDateTime.format(dateFormatter);

    }

    public static LocalDateTime getLocalUTC0String(String pattern){
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));

        return utcNow.toLocalDateTime();
    }

    public static long getNowUnixTime() {

        return ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
    }

    public static long getUnixTime(
            String stringDateTime,
            String pattern
    ) {

        LocalDateTime localDateTime = LocalDateTime.parse(
                stringDateTime,
                dateTimeFormatter(pattern)
        );

        return localDateTime.atZone(ZoneOffset.UTC).toEpochSecond();
    }

    public static long getNowDiffTime(
            String stringDateTime,
            String pattern
    ) {

        long utcUnixTime = getUnixTime(
                stringDateTime,
                pattern
        );

        return utcUnixTime - getNowUnixTime();
    }

    public static Date getPresignedExpireDate(){
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

        ZonedDateTime oneMinuteLater = nowUTC.plusMinutes(5);

        return Date.from(oneMinuteLater.toInstant());

    }

    public static LocalDateTime getTodayStartDate(String date, String pattern) {

        return parsedStringToLocaleDate(date + " 00:00:00", pattern);
    }

    public static LocalDateTime getTodayEndDate(String date, String pattern) {

        return parsedStringToLocaleDate(date + " 23:59:59", pattern);
    }

    public static LocalDateTime parsedStringToLocaleDate(String date, String pattern) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(date, formatter);
    }

    public static String replaceDateForT(
            String date
    ){

        if(date.contains("T")) {
            return date.replace("T", " ");
        }
        return date;
    }

    public static String getAddHours(
            String dateStr,
            Integer hour
    ){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yyyy_MM_DD_HH_mm_ss);

        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        Duration durationToAdd = Duration.ofHours(hour);

        LocalDateTime newDateTime = dateTime.plus(durationToAdd);

        return newDateTime.format(formatter);

    }

    public static String replacePlainTimeFromLocaleDate(LocalDateTime date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yyyy_MM_DD_HH_mm_ss);
        return getAddHours(date.format(formatter), -9);

    }

}
