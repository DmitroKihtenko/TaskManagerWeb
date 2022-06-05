package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

public class NonZeroAmount extends Amount {
    public NonZeroAmount(String parameter, Integer value, boolean mandatory) {
        super(parameter, value, mandatory);
    }

    public static boolean verify(Integer value, boolean throwError) {
        boolean verified = value >= 0;
        if(!verified && throwError) {
            throw new IllegalArgumentException(
                    "Non zero amount must be positive only"
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
