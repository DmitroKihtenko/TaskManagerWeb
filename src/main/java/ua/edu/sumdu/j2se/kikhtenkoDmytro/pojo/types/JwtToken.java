package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class JwtToken extends VerifiedCustomType<String> {
    private static final Pattern pattern =
            Pattern.compile("^.{1,1000}$");
    public static boolean verify(String value, boolean throwError) {
        boolean verified = pattern.matcher(value).find();
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Token value must be a string with length " +
                            "1-1000 symbols"
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

    public JwtToken(@NonNull String parameter, String value,
                    boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
