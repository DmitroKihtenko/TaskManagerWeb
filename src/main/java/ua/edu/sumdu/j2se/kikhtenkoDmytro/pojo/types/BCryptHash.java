package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class BCryptHash extends VerifiedCustomType<String> {
    private static final Pattern pattern =
            Pattern.compile("^.{60}$");
    public static boolean verify(String value, boolean throwError) {
        boolean verified = pattern.matcher(value).find();
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Password hash must have length of 60 symbols only"
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

    public BCryptHash(@NonNull String parameter,
                      String value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
