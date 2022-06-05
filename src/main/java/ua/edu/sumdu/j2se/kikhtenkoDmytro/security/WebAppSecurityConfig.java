package ua.edu.sumdu.j2se.kikhtenkoDmytro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt.JwtAuthenticationFilter;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt.JwtAuthenticationProvider;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtAuthenticationFilter jwtFilter;

    private final ExceptionFilter exceptionFilter;
    private final JwtAuthenticationProvider jwtProvider;

    @Autowired
    public WebAppSecurityConfig(
            @NonNull UserService accessService,
            @NonNull JwtAuthenticationFilter jwtFilter,
            @NonNull JwtAuthenticationProvider jwtProvider,
            @NonNull ExceptionFilter exceptionFilter) {
        this.userService = accessService;
        this.jwtFilter = jwtFilter;
        this.jwtProvider = jwtProvider;
        this.exceptionFilter = exceptionFilter;
    }

    @NonNull
    public UserService getUserService() {
        return userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(jwtProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity.sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and();
        httpSecurity.csrf().disable();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(
                Arrays.asList("GET","POST","PATCH","DELETE"));
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        httpSecurity.cors().configurationSource(source);

        httpSecurity.authorizeRequests().
                antMatchers("/signIn", "/health").permitAll().
                anyRequest().authenticated().
                and().
                logout().disable().
                formLogin().disable();
        httpSecurity.addFilterBefore(
                jwtFilter, AnonymousAuthenticationFilter.class
        );
        httpSecurity.addFilterAfter(
                exceptionFilter, ExceptionTranslationFilter.class
        );
    }
}
