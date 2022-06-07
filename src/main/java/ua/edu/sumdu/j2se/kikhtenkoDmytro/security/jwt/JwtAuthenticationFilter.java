package ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Token;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private JwtProcessor jwtProcessor;

    public JwtAuthenticationFilter(
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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);
        if(header != null) {
            Token token = getTokenFromHeader(header);
            if(token != null) {
                SecurityContextHolder.getContext().setAuthentication(
                        new JwtAuthentication(token));
            }
        }
        chain.doFilter(request, response);
    }

    public Token getTokenFromHeader(@NonNull String value) {
        Token token = null;
        String[] tokenParts = value.split(" ");
        if(tokenParts.length == 2) {
            token = new Token();
            token.setType(tokenParts[0]);
            token.setJwtToken(tokenParts[1]);
        }
        return token;
    }
}
