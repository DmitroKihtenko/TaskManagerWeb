package ua.edu.sumdu.j2se.kikhtenkoDmytro.security;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

public enum Authority implements GrantedAuthority, Serializable {
    USERS_EDIT("USERS_EDIT"),
    USERS_READ("USERS_READ"),
    TASKS("TASKS"),
    CREDS_EDIT("CREDS_EDIT");
    public static final int ROLES = 4;
    private final String roleName;

    Authority(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

    @JsonValue
    public int getId() {
        return ordinal();
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    public String getReadableName() {
        switch(this) {
            case USERS_EDIT:
                return "Users editing";
            case USERS_READ:
                return "Users reading";
            case TASKS:
                return "Tasks";
            default:
                return "Credentials editing";
        }
    }

    public String getDescription() {
        switch(this) {
            case USERS_EDIT:
                return "Creating editing and deleting all users";
            case USERS_READ:
                return "Viewing list of all users and viewing any" +
                        " user data";
            case TASKS:
                return "Viewing and editing your own tasks";
            default:
                return "Managing your account authentication data";
        }
    }

    @NonNull
    public static Authority getAuthority(int id) {
        for(Authority authority : getAllAuthorities()) {
            if(authority.getId() == id) {
                return authority;
            }
        }
        throw new IllegalArgumentException(
                "Authority with id '" + id + "' not found"
        );
    }

    public static HashSet<Authority> getAllAuthorities() {
        HashSet<Authority> allAuthorities = new HashSet<>(ROLES);
        allAuthorities.addAll(Arrays.asList(Authority.values()));
        return allAuthorities;
    }
}
