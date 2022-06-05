package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

public class ObjectType<T> extends VerifiedCustomType<T> {
    public ObjectType(@NonNull String parameter,
                      T value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
