package ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Token;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class JwtAuthentication implements Authentication {
    private Boolean authenticated;
    private Token token;

    public JwtAuthentication(@NonNull Token token) {
        setToken(token);
    }

    @NonNull
    public Token getToken() {
        return token;
    }

    public void setToken(@NonNull Token token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public boolean isAuthenticated() {
        return Objects.requireNonNullElse(authenticated,false);
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated = b;
    }

    @Override
    public String getName() {
        return token.getJwtToken();
    }
}
