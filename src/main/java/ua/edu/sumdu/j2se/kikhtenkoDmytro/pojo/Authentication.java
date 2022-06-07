package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Identity;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Password;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

import java.util.Objects;

public class Authentication extends Identification {
    private final VerifiedCustomType<String> password;

    protected VerifiedCustomType<String> createNameCover() {
        return new Identity("Name", null,true);
    }

    protected VerifiedCustomType<String> createPasswordCover() {
        return new Password("Password", null,true);
    }

    public Authentication() {
        password = createPasswordCover();
    }

    public String getPassword() {
        return password.getValue();
    }

    public void setPassword(String password) {
        this.password.setValue(password);
        this.password.setAdjusted(true);
    }

    @JsonIgnore
    @NonNull
    public VerifiedCustomType<String> getPasswordCover() {
        return password;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                super.toString() +
                ", password=" + password +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authentication)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Authentication loginData = (Authentication) o;
        return password.equals(loginData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), password);
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof Authentication) {
            Authentication auth = (Authentication) object;
            if(auth.getPasswordCover().isAdjusted()) {
                setPassword(auth.getPassword());
            }
        }
    }

    @Override
    public Authentication replicate() {
        Authentication value = new Authentication();
        value.update(this);
        return value;
    }
}
