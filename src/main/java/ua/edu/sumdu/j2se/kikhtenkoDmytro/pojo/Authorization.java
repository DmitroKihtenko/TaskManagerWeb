package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.BooleanType;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.ObjectType;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;

import java.util.*;

public class Authorization extends Authentication implements UserDetails {
    private final BooleanType enabled;
    private final ObjectType<Set<Authority>> authorities;

    public Authorization() {
        authorities = new ObjectType<>(
                "User authorities", new HashSet<>(), true);
        enabled = new BooleanType("Enabled", null,true);
    }

    @JsonSetter
    public void setEnabled(@Nullable Boolean enabled) {
        this.enabled.setValue(enabled);
        this.enabled.setAdjusted(true);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesSet();
    }

    @JsonSetter
    public void setAuthorities(@Nullable Authority... authorities) {
        if(authorities == null) {
            this.authorities.setValue(null);
        } else {
            if(this.authorities.getValue() == null) {
                this.authorities.setValue(new HashSet<>());
            }
            this.authorities.getValue().clear();
            for (Authority authority : authorities) {
                this.authorities.getValue().add(authority);
            }
        }
        this.authorities.setAdjusted(true);
    }

    public void setAuthorities(@Nullable Iterable<Authority> authorities) {
        if(authorities == null) {
            this.authorities.setValue(null);
        } else {
            if(this.authorities.getValue() == null) {
                this.authorities.setValue(new HashSet<>());
            }
            this.authorities.getValue().clear();
            for (Authority authority : authorities) {
                this.authorities.getValue().add(authority);
            }
        }
        this.authorities.setAdjusted(true);
    }

    public void addAuthority(@NonNull Authority authority) {
        if(authorities.getValue() == null) {
            authorities.setValue(new HashSet<>());
        }
        authorities.getValue().add(authority);
        this.authorities.setAdjusted(true);
    }

    public void removeAuthority(@NonNull Authority authority) {
        if(authorities.getValue() == null) {
            authorities.setValue(new HashSet<>());
        }
        authorities.getValue().remove(authority);
        this.authorities.setAdjusted(true);
    }

    public void clearAuthorities() {
        if(authorities.getValue() != null) {
            authorities.getValue().clear();
        }
        this.authorities.setAdjusted(true);
    }

    @JsonIgnore
    @NonNull
    public Set<Authority> getAuthoritiesSet() {
        if(authorities.getValue() == null) {
            return new HashSet<>();
        } else {
            return authorities.getValue();
        }
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getName();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return Objects.requireNonNullElse(enabled.getValue(), false);
    }

    @JsonGetter
    @Nullable
    public Boolean getEnabled() {
        return enabled.getValue();
    }

    @JsonIgnore
    @NonNull
    public ObjectType<Set<Authority>> getAuthoritiesCover() {
        return authorities;
    }

    @JsonIgnore
    @NonNull
    public BooleanType getEnabledCover() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authorization)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Authorization that = (Authorization) o;
        return enabled.equals(that.enabled) &&
                authorities.equals(that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), enabled, authorities);
    }

    @Override
    public String toString() {
        return "Authorization{" +
                super.toString() +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                "}, " + super.toString();
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof Authorization) {
            Authorization access = (Authorization) object;
            if(access.getEnabledCover().isAdjusted()) {
                setEnabled(access.getEnabled());
            }
            if(access.getAuthoritiesCover().isAdjusted()) {
                setAuthorities(access.getAuthoritiesSet());
            }
        }
    }

    @Override
    public Authorization replicate() {
        Authorization value = new Authorization();
        value.update(this);
        return value;
    }
}
