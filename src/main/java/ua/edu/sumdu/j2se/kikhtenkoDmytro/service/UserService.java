package ua.edu.sumdu.j2se.kikhtenkoDmytro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.*;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.EntityQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.UserQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.UserAuthentication;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt.JwtProcessor;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.ServiceExceptionWrapper;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Primary
@Scope(
        scopeName = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class UserService extends EntityService<UserData> implements UserDetailsService {
    private PasswordEncoder passwordEncoder;
    private JwtProcessor jwtProcessor;

    @Autowired
    public UserService(@NonNull PasswordEncoder
                                   passwordEncoder,
                       @NonNull JwtProcessor jwtProcessor,
                       @NonNull UserQueries accessQueries,
                       @NonNull ServiceExceptionWrapper wrapper
    ) {
        super(accessQueries, wrapper);
        setPasswordEncoder(passwordEncoder);
        setJwtProcessor(jwtProcessor);
    }

    @Resource(type = UserQueries.class)
    @Override
    public void setEntityQueries(
            EntityQueries<UserData> entityQueries) {
        super.setEntityQueries(entityQueries);
    }

    @NonNull
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(@NonNull PasswordEncoder
                                           passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @NonNull
    public JwtProcessor getJwtProcessor() {
        return jwtProcessor;
    }

    public void setJwtProcessor(@NonNull JwtProcessor jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    public UserData authorize(@NonNull Authentication auth, boolean withPassword)
            throws ServiceException {
        if(auth.getName() == null) {
            throw new ServiceException(
                    "Required username for identification",
                    HttpStatus.BAD_REQUEST
            );
        }
        if(auth.getPassword() == null) {
            throw new ServiceException(
                    "Required password for authentication"
            );
        }
        UserData databaseUser = get(auth.getName(),
                true, true);
        if(!passwordEncoder.matches(auth.getPassword(),
                databaseUser.getPassword())) {
            throw new ServiceException(
                    "Invalid password",
                    HttpStatus.BAD_REQUEST);
        }
        if(!databaseUser.isEnabled()) {
            throw new ServiceException(
                    "User disabled",
                    HttpStatus.BAD_REQUEST);
        }
        if(!withPassword) {
            databaseUser.setPassword(null);
        }
        return databaseUser;
    }

    public UserData authorize(@NonNull Authentication auth) throws ServiceException {
        return authorize(auth, false);
    }

    @NonNull
    public Token createToken(@NonNull UserData userAccess)
            throws ServiceException {
        try {
            UserAuthentication authentication =
                    new UserAuthentication();
            authentication.setId(userAccess.getId());
            authentication.setName(userAccess.getName());
            authentication.setAuthorities(userAccess.
                    getAuthoritiesSet());
            authentication.setAuthenticated(true);
            return jwtProcessor.createToken(authentication);
        } catch (IOException e) {
            throw new ServiceException(
                    "Token creating error. " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public UserAuthentication getCurrentAuthentication() {
        return (UserAuthentication) SecurityContextHolder.
                getContext().getAuthentication();
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String s)
            throws UsernameNotFoundException {
        try {
            UserData userAccess = get(s, true, false);
            if(userAccess == null) {
                throw new UsernameNotFoundException(
                        "User '" + s + "' not found"
                );
            }
            return userAccess;
        } catch (ServiceException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public UserData authorize(@NonNull Authorization auth, boolean withPassword)
            throws ServiceException {
        if(auth.getName() == null) {
            throw new ServiceException(
                    "Required username for identification",
                    HttpStatus.BAD_REQUEST
            );
        }
        if(auth.getPassword() == null) {
            throw new ServiceException(
                    "Required password for authentication"
            );
        }
        UserData userAccess = get(
                auth.getName(), true, true);
        if(!passwordEncoder.matches(auth.getPassword(),
                userAccess.getPassword())) {
            throw new ServiceException(
                    "Invalid password",
                    HttpStatus.BAD_REQUEST);
        }
        if(!withPassword) {
            userAccess.setPassword(null);
        }
        return userAccess;
    }

    public UserData authorize(@NonNull Authorization auth) throws ServiceException {
        return authorize(auth, false);
    }

    @Override
    @Nullable
    public UserData get(@NonNull int id)
            throws ServiceException {
        return get(id, false, false);
    }

    public UserData get(@NonNull String username)
            throws ServiceException {
        return get(username, false, false);
    }

    @Nullable
    public UserData get(
            int id,
            boolean withPassword,
            boolean errorOnNotFound
    ) throws ServiceException {
        UserData userAccess = null;
        UserQueries queries = (UserQueries) getEntityQueries();
        try {
            getEntityQueries().getProvider().connect();
            userAccess = queries.get(id, withPassword);
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            getEntityQueries().getProvider().disconnect();
        }
        if(errorOnNotFound && userAccess == null) {
            throw new ServiceException(
                    "User with id '" + id + "' not found",
                    HttpStatus.BAD_REQUEST
            );
        }
        return userAccess;
    }

    @Nullable
    public UserData get(
            @NonNull String username,
            boolean withPassword,
            boolean errorOnNotFound
    ) throws ServiceException {
        UserData userAccess = null;
        UserQueries queries = (UserQueries) getEntityQueries();
        try {
            getEntityQueries().getProvider().connect();
            userAccess = queries.get(username, withPassword);
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            getEntityQueries().getProvider().disconnect();
        }
        if(errorOnNotFound && userAccess == null) {
            throw new ServiceException(
                    "User '" + username + "' not found",
                    HttpStatus.BAD_REQUEST
            );
        }
        return userAccess;
    }

    public void add(@NonNull Authorization authorization)
            throws ServiceException {
        UserData userData = new UserData();
        updateUserData(authorization, userData);
        add(userData);
    }

    public void addAndGet(UserData userData) throws ServiceException {
        UserQueries queries = (UserQueries) getEntityQueries();
        try {
            queries.getProvider().connect();
            queries.insert(userData);
            userData.update(queries.get(userData.getName(), false));
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            queries.getProvider().disconnect();
        }
    }

    public void update(int id, @NonNull UserData userData)
            throws ServiceException {
        UserData lastAccess = get(
                id, true, true);
        lastAccess.update(userData);
        EntityQueries<UserData> queries = getEntityQueries();
        try {
            queries.getProvider().connect();
            queries.update(id, lastAccess);
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            queries.getProvider().disconnect();
        }
        jwtProcessor.getSecretKeeper().resetSecret(lastAccess);
    }

    public void update(int id, @NonNull Authorization authorization)
            throws ServiceException {
        UserData lastAccess = get(
                id, true, true);
        updateUserData(authorization, lastAccess);
        EntityQueries<UserData> queries = getEntityQueries();
        try {
            queries.getProvider().connect();
            queries.update(id, lastAccess);
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            queries.getProvider().disconnect();
        }
        jwtProcessor.getSecretKeeper().resetSecret(lastAccess);
    }

    public void update(int id, @NonNull Authentication authentication)
            throws ServiceException {
        Authorization authorization = new Authorization();
        authorization.update(authentication);
        update(id, authorization);
    }

    @Override
    public void delete(int key) throws ServiceException {
        super.delete(key);
        IdHolder holder = new IdHolder();
        holder.setId(key);
        jwtProcessor.getSecretKeeper().resetSecret(holder);
    }

    @NonNull
    public void updateUserData(
            @NonNull Authorization authorization,
            @NonNull UserData userData
    ) {
        if(authorization.getIdCover().isAdjusted()) {
            userData.setId(authorization.getId());
        }
        if(authorization.getNameCover().isAdjusted()) {
            userData.setName(authorization.getUsername());
        }
        if(authorization.getPasswordCover().isAdjusted()) {
            userData.setPassword(passwordEncoder.encode(
                    authorization.getPassword()));
        }
        if(authorization.getEnabledCover().isAdjusted()) {
            userData.setEnabled(authorization.getEnabled());
        }
        if(authorization.getAuthoritiesCover().isAdjusted()) {
            userData.setAuthorities(authorization.getAuthoritiesSet());
        }
    }
}
