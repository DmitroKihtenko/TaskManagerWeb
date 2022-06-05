package ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;

public class CastAssertions {
    public static int stringToInt(@NonNull String number,
                                  @NonNull String parameter) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(parameter + " parameter" +
                    " has invalid integer format");
        }
    }

    @NonNull
    public static LocalDate stringToDate(@NonNull String date,
                                         @NonNull String parameter,
                                         @NonNull DateTimeFormatter
                                             format) {
        try {
            return LocalDate.parse(date, format);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(parameter +
                    " parameter has invalid date format");
        }
    }

    @NonNull
    public static Set<Integer> stringToNumbers(
            @NonNull String numbers,
            @NonNull String parameter) {
        Set<Integer> numbersSet = new HashSet<>();
        try {
            for(String numberString : numbers.split(",")) {
                numbersSet.add(Integer.parseInt(numberString));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(parameter +
                    " parameter has invalid " +
                    "integer numbers list format");
        }
        return numbersSet;
    }

    @NonNull
    public static String numbersToString(
            @NonNull Iterable<Integer> numbers) {
        StringBuilder value = new StringBuilder();
        boolean firstNumber = true;
        for(Integer number : numbers) {
            if(firstNumber) {
                firstNumber = false;
                value.append(number);
            } else {
                value.append(",").append(number);
            }
        }
        return value.toString();
    }

    public static int boolToInt(boolean value) {
        if(value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean intToBool(int value) {
        return value != 0;
    }

    public static String boolToChar(boolean value) {
        if(value) {
            return "t";
        } else {
            return "f";
        }
    }

    public static boolean charToBool(@NonNull String value,
                                     @NonNull String parameter) {
        if(value.equals("t")) {
            return true;
        } else if (value.equals("f")) {
            return false;
        } else {
            throw new ClassCastException(
                    parameter + " can not be converted to boolean " +
                            "with value '" + value + "'"
            );
        }
    }

    public static boolean stringToBool(@NonNull String value,
                                       @NonNull String parameter) {
        if(value.equals("true")) {
            return true;
        } else if(value.equals("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(
                    parameter + " can not be converted to boolean " +
                    "with value '" + parameter + "'");
        }
    }

    @NonNull
    public static String boolToString (boolean value) {
        if(value) {
            return "true";
        } else {
            return "false";
        }
    }
}
