package com.coffee.util;

import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeConverterUtil {

    public final String DF1 = "yyyy-MM-dd HH:mm:ss";
    public final String DF2 = "dd/MM/yyyy";
    public final String DF3 = "dd/MM/yyyy HH:mm:ss";
    public final String DF4 = "dd/MM/yyyy HH:mm";

    private static final DateTimeFormatter MYSQL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DF1);

    private static final DateTimeFormatter DATE_FORMAT_1 = DateTimeFormatter.ofPattern(DF2);

    private static final DateTimeFormatter DATE_FORMAT_2 = DateTimeFormatter.ofPattern(DF3);

    private static final DateTimeFormatter DATE_FORMAT_3 = DateTimeFormatter.ofPattern(DF4);


    public static String formatToMySqlDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(MYSQL_DATETIME_FORMATTER);
    }

    public static LocalDateTime parseFromMySqlDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isBlank()) return null;
        return LocalDateTime.parse(dateTimeString, MYSQL_DATETIME_FORMATTER);
    }

    public static String formatToDate_ddMMyyyy(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_FORMAT_1);
    }

    public static String formatToDate_ddMMyyyyHHmmss(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_FORMAT_2);
    }

    public static String formatToDate_ddMMyyyyHHmm(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DATE_FORMAT_3);
    }

    public static LocalDateTime parseFromDate_ddMMyyyy(String dateString) {
        if (dateString == null || dateString.isBlank()) return null;
        return LocalDateTime.parse(dateString, DATE_FORMAT_1);
    }

    public static LocalDateTime parseFromDate_ddMMyyyyHHmmss(String dateString) {
        if (dateString == null || dateString.isBlank()) return null;
        return LocalDateTime.parse(dateString, DATE_FORMAT_2);
    }

    public static LocalDateTime parseFromDate_ddMMyyyyHHmm(String dateString) {
        if (dateString == null || dateString.isBlank()) return null;
        return LocalDateTime.parse(dateString, DATE_FORMAT_3);
    }


    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = formatToMySqlDateTime(now);
        System.out.println("Formatted: " + formatted); // e.g., "2025-08-29 14:45:30"

        LocalDateTime parsedBack = parseFromMySqlDateTime(formatted);
        System.out.println("Parsed: " + parsedBack); // e.g., 2025-08-29T14:45:30

    }

    public static OffsetDateTime convertToISTDateTimeStringFromUTCDateTimeString(LocalDateTime createdAt) {
        if (createdAt == null) return null;
        return createdAt // LocalDateTime
                .atZone(ZoneOffset.UTC) // Treat as UTC
                .withZoneSameInstant(ZoneId.of("Asia/Kolkata")) // Convert to IST
                .toOffsetDateTime();
    }
}
