package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.InfoResponse;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public InfoResponse handleError(
            @NonNull HttpServletResponse response,
            @NonNull ServiceException e) {
        response.setStatus(e.getLastStatus().value());
        return new InfoResponse(e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public InfoResponse handleUserError(
            @NonNull HttpServletResponse response,
            @NonNull RuntimeException e
    ) {
        String message = e.getMessage();
        ServiceException wrapper = new ServiceException(
                message, HttpStatus.BAD_REQUEST);
        if(e instanceof AuthenticationCredentialsNotFoundException) {
            wrapper = new ServiceException(
                    "Authorization token required",
                    HttpStatus.UNAUTHORIZED);
        } else if(e instanceof AuthenticationException) {
            wrapper = new ServiceException(message,
                    HttpStatus.UNAUTHORIZED);
        } else if(e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException casted =
                    (HttpMessageNotReadableException) e;
            if(casted.getRootCause() != null) {
                message = casted.getRootCause().getMessage();
            }
            wrapper = new ServiceException(message,
                    HttpStatus.BAD_REQUEST);
        }
        return handleError(response, wrapper);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public InfoResponse handleServerError(
            @NonNull HttpServletResponse response,
            @NonNull Exception e
    ) {
        String message = e.getMessage();
        ServiceException wrapper = new ServiceException(
                message, HttpStatus.INTERNAL_SERVER_ERROR);
        if(e instanceof AccessDeniedException) {
            wrapper = new ServiceException(message, HttpStatus.FORBIDDEN);
        } else if(e instanceof NoHandlerFoundException) {
            wrapper = new ServiceException(message, HttpStatus.NOT_FOUND);
        } else if(e instanceof HttpMediaTypeNotSupportedException) {
            wrapper = new ServiceException(e.getMessage(),
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return handleError(response, wrapper);
    }
}
