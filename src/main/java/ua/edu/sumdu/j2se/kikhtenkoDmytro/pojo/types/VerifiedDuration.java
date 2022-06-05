package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import java.time.Duration;

public class VerifiedDuration extends ObjectType<Duration> {
    public static boolean verify(Duration value, boolean throwError) {
        boolean verified = true;
        if(value != null) {
            verified = !value.isNegative() && !value.isZero();
        }
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Duration can be positive only"
            );
        }
        return verified;
    }

    public static boolean verify(Duration value) {
        return verify(value, true);
    }
    public VerifiedDuration(String parameter, Duration value, boolean mandatory) {
        super(parameter, value, mandatory);
    }

    @Override
    protected void checkValue(Duration value) {
        super.checkValue(value);
        verify(value);
    }
}
