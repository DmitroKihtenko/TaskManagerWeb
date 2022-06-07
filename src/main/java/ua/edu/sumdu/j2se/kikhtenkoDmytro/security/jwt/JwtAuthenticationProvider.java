package ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.JwtAuthenticationException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.UserAuthentication;

import java.io.IOException;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private JwtProcessor jwtProcessor;

    public JwtAuthenticationProvider(
            @NonNull JwtProcessor jwtProcessor) {
        setJwtProcessor(jwtProcessor);
    }

    @NonNull
    public JwtProcessor getJwtProcessor() {
        return jwtProcessor;
    }

    public void setJwtProcessor(@NonNull JwtProcessor jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        UserAuthentication newAuthentication;
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        try {
            newAuthentication = jwtProcessor.parseToken(
                    jwtAuthentication.getToken());
        } catch (IOException e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
        return newAuthentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtAuthentication.class);
    }
}
