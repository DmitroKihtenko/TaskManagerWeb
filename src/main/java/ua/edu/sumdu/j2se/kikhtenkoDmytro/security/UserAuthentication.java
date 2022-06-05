package ua.edu.sumdu.j2se.kikhtenkoDmytro.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.IdHolder;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class UserAuthentication extends IdHolder implements Authentication {
    private final VerifiedCustomType<String> name;
    private final VerifiedCustomType<Boolean> authenticated;
    private final ObjectType<HashSet<Authority>> authorities;
    private final ObjectType<Object> details;

    public UserAuthentication() {
        name = new Identity("Name", null, true);;
        authenticated = new BooleanType("Authenticated", null,false);
        authorities = new ObjectType<>("Authorities", null,false);
        details = new ObjectType<>("Details", null,false);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
        this.name.setAdjusted(true);
    }

    @JsonIgnore
    public VerifiedCustomType<String> getNameCover() {
        return name;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        authenticated.setValue(b);
        authenticated.setAdjusted(true);
    }

    @JsonSetter
    public void setAuthenticated(Boolean b) {
        authenticated.setValue(b);
        authenticated.setAdjusted(true);
    }

    public void setAuthorities(Iterable<Authority> authorities) {
        if(this.authorities.getValue() == null) {
            this.authorities.setValue(new HashSet<>());
        }
        this.authorities.getValue().clear();
        for(Authority authority : authorities) {
            this.authorities.getValue().add(authority);
        }
        this.authorities.setAdjusted(true);
    }

    public void setDetails(Object details) {
        this.details.setValue(details);
        this.details.setAdjusted(true);
    }

    @JsonIgnore
    @Override
    public Object getCredentials() {
        return getName();
    }

    @Override
    public Object getDetails() {
        return getName();
    }

    @JsonIgnore
    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities.getValue() == null) {
            return new ArrayList<>();
        } else {
            return authorities.getValue();
        }
    }

    @JsonIgnore
    public HashSet<Authority> getAuthoritiesSet() {
        if(authorities.getValue() == null) {
            return new HashSet<>();
        } else {
            return authorities.getValue();
        }
    }

    @JsonIgnore
    @Override
    public boolean isAuthenticated() {
        return Objects.requireNonNullElse(authenticated.getValue(),
                false);
    }

    public Boolean getAuthenticated() {
        return authenticated.getValue();
    }

    @JsonIgnore
    @NonNull
    public ObjectType<HashSet<Authority>> getAuthoritiesCover() {
        return authorities;
    }

    @JsonIgnore
    @NonNull
    public VerifiedCustomType<Boolean> getAuthenticatedCover() {
        return authenticated;
    }

    @JsonIgnore
    @NonNull
    public ObjectType<Object> getDetailsCover() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthentication) || !super.equals(o)) {
            return false;
        }
        UserAuthentication that = (UserAuthentication) o;
        return authenticated.equals(that.authenticated) &&
                authorities.equals(that.authorities) &&
                details.equals(that.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authenticated,
                authorities, details);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                super.toString() +
                ", authenticated=" + authenticated +
                ", authorities=" + authorities +
                ", details=" + details +
                '}';
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof UserAuthentication) {
            UserAuthentication access =
                    (UserAuthentication) object;
            if(access.getNameCover().isAdjusted()) {
                setName(access.getName());
            }
            if(access.getAuthenticatedCover().isAdjusted()) {
                setAuthenticated(access.getAuthenticated());
            }
            if(access.getAuthoritiesCover().isAdjusted()) {
                setAuthorities(access.getAuthoritiesSet());
            }
            if(access.getDetailsCover().isAdjusted()) {
                setDetails(access.getDetails());
            }
        }
    }

    @Override
    public UserAuthentication replicate() {
        UserAuthentication value = new UserAuthentication();
        value.update(this);
        return value;
    }
}
