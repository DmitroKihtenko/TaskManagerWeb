package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao;

import org.springframework.lang.NonNull;

public interface QueryLoader {
    @NonNull
    String load(@NonNull String key) throws Exception;
}
