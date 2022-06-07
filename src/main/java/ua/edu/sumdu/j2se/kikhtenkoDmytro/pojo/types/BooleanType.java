package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

public class BooleanType extends VerifiedCustomType<Boolean> {
    public BooleanType(@NonNull String parameter,
                       Boolean value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
