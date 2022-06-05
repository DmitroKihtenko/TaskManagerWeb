package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import org.springframework.lang.NonNull;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;

import java.util.Objects;

public class AuthorityData {
    private Authority authority;

    public AuthorityData(int id) {
        setId(id);
    }

    public AuthorityData(@NonNull Authority authority) {
        this.authority = authority;
    }

    public void setId(int id) {
        authority = Authority.getAuthority(id);
    }

    public int getId() {
        return authority.getId();
    }

    public String getName() {
        return authority.getReadableName();
    }

    public String getDescription() {
        return authority.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityData)) {
            return false;
        }
        AuthorityData that = (AuthorityData) o;
        return authority == that.authority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }

    @Override
    public String toString() {
        return "AuthorityData{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                '}';
    }
}
