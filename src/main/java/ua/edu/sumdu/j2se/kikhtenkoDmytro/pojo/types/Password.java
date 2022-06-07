package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class Password extends VerifiedCustomType<String> {
    private final static Pattern pattern =
            Pattern.compile("^[\\w\\d_!@$%^&]{8,24}$");

    public Password(@NonNull String parameter,
                    String value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
    public static boolean verify(String value, boolean throwError) {
        boolean verified = pattern.matcher(value).find();
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Password can only have latin symbols," +
                            " numbers and symbols _!@$%^& with" +
                            " length 8-24 symbols"
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
