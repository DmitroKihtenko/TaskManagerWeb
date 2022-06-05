package ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.oracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.ServiceExceptionWrapper;

import java.sql.SQLException;

public class OracleExceptionWrapper implements ServiceExceptionWrapper {
    private static final Logger logger =
            LoggerFactory.getLogger(OracleExceptionWrapper.class);

    @Override
    @Nullable
    public ServiceException handle(@NonNull Exception e) {
        ServiceException result = null;
        if(e instanceof SQLException) {
            SQLException exception = (SQLException) e;
            HttpStatus defaultStatus = HttpStatus.BAD_REQUEST;
            switch (exception.getErrorCode()) {
                case 1:
                    result = new ServiceException(
                            "Entity already exist",
                            HttpStatus.CONFLICT);
                    break;
                case 2291:
                    result = new ServiceException(
                            "Nested entity does not exist",
                            defaultStatus);
                    break;
                case 1400:
                    result = new ServiceException(
                            "Required values are not set",
                            defaultStatus);
            }
        }
        if(result != null) {
            logger.debug("Handled database error: " + e);
        }
        return result;
    }
}
