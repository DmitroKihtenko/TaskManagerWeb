package ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;

public interface ServiceExceptionWrapper {
    @Nullable
    ServiceException handle(@NonNull Exception exception);

    default void wrap(@NonNull Exception exception)
            throws ServiceException {
        ServiceException wrapped = handle(exception);
        if(wrapped == null) {
            throw new ServiceException(
                    "Unknown server error",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } else {
            throw wrapped;
        }
    }
}
