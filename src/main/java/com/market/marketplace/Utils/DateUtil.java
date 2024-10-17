package com.market.marketplace.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public static String localDateToString(LocalDate date) {
        return (date != null) ? date.format(FORMATTER) : "No Date";
    }

    public static LocalDate stringToLocalDate(String dateString) {
        try {
            return (dateString != null && !dateString.isEmpty()) ? LocalDate.parse(dateString, FORMATTER) : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
