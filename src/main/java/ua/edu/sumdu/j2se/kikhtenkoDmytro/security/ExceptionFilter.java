package ua.edu.sumdu.j2se.kikhtenkoDmytro.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private HandlerExceptionResolver exceptionResolver;

    public ExceptionFilter(@NonNull HandlerExceptionResolver
                                   exceptionResolver) {
        setExceptionResolver(exceptionResolver);
    }

    @NonNull
    public HandlerExceptionResolver getExceptionResolver() {
        return exceptionResolver;
    }

    public void setExceptionResolver(@NonNull HandlerExceptionResolver
                                             exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.debug("Filter chain exception: " + e);
            exceptionResolver.resolveException(
                    request, response, null, e);
        }
    }
}
