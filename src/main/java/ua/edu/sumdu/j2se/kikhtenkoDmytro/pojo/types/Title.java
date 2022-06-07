package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class Title extends VerifiedCustomType<String> {
    private final static Pattern pattern =
            Pattern.compile("^[\\w \\d]{1,100}$");

    public Title(@NonNull String parameter,
                 String value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
    public static boolean verify(String value, boolean throwError) {
        boolean verified = pattern.matcher(value).find();
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Title can have only alphabetic symbols, " +
                            "numbers and spaces with length 1-100"
            );
        }
        return verified;
    }

    public static boolean verify(String value) {
        return verify(value, true);
    }
    @Override
    protected void checkValue(String value) {
        super.checkValue(value);
        if(value != null) {
            verify(value);
        }
    }
}
