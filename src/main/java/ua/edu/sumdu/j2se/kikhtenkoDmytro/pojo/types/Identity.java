package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class Identity extends VerifiedCustomType<String> {
    private static final Pattern pattern =
            Pattern.compile("^[\\w\\d_]{2,24}$");

    public Identity(@NonNull String parameter,
                    String value, boolean mandatory) {
        super(parameter, value, mandatory);
    }

    public static boolean verify(String value, boolean throwError) {
        boolean verified = pattern.matcher(value).find();
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Identity can have " +
                            "latin symbols, numbers and symbol '_'" +
                            " with length of 2-24 symbols"
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
