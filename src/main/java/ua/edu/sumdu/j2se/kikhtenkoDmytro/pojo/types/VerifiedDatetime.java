package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import java.time.LocalDateTime;

public class VerifiedDatetime extends ObjectType<LocalDateTime> {
    public VerifiedDatetime(String parameter, LocalDateTime value, boolean mandatory) {
        super(parameter, value, mandatory);
    }
}
