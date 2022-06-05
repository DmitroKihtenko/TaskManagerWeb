package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

public class Identifier extends VerifiedCustomType<Integer> {
    public Identifier(@NonNull String parameter,
                      Integer value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
