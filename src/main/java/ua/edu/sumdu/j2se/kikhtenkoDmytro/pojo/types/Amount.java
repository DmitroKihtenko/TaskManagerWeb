package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

public class Amount extends VerifiedCustomType<Integer> {
    public Amount(@NonNull String parameter,
                  Integer value, boolean mandatory) {
        super(parameter, value, mandatory);
    }

    public static boolean verify(Integer value, boolean throwError) {
        boolean verified = value >= 0;
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Amount value must be non-negative"
            );
        }
        return verified;
    }

    public static boolean verify(Integer value) {
        return verify(value, true);
    }

    @Override
    protected void checkValue(Integer value) {
        super.checkValue(value);
        if(value != null) {
            verify(value);
        }
    }
}
