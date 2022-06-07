package ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ServiceException extends Exception {
    private final HttpStatus lastStatus;
    private final Object content;

    public ServiceException() {
        lastStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        content = null;
    }

    public ServiceException(String message) {
        super(message);
        lastStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        content = null;
    }

    public ServiceException(HttpStatus lastStatus) {
        this.lastStatus = lastStatus;
        content = null;
    }

    public ServiceException(String message, HttpStatus lastStatus) {
        super(message);
        this.lastStatus = lastStatus;
        content = null;
    }

    public ServiceException(String message, HttpStatus lastStatus,
                            Object content) {
        super(message);
        this.lastStatus = lastStatus;
        this.content = content;
    }

    public ServiceException(String message, Throwable cause, HttpStatus lastStatus) {
        super(message, cause);
        this.lastStatus = lastStatus;
        content = null;
    }

    public ServiceException(Throwable cause, HttpStatus lastStatus) {
        super(cause);
        this.lastStatus = lastStatus;
        content = null;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus lastStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.lastStatus = lastStatus;
        content = null;
    }

    @Nullable
    public Object getContent() {
        return content;
    }

    @NonNull
    public HttpStatus getLastStatus() {
        return lastStatus;
    }

    public int getLastResponseCode() {
        return lastStatus.value();
    }
}
