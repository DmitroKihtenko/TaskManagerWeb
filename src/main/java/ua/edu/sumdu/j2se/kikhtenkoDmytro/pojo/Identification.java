package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Identity;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Password;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

import java.util.Objects;

public class Identification extends IdHolder {

    private final VerifiedCustomType<String> name;

    protected VerifiedCustomType<String> createNameCover() {
        return new Identity("Name", null,true);
    }

    VerifiedCustomType<String> createPasswordCover() {
        return new Password("Password", null,true);
    }

    public Identification() {
        name = createNameCover();
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
    public String toString() {
        return "Identification{" +
                super.toString() +
                ", name=" + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Identification)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Identification loginData = (Identification) o;
        return name.equals(loginData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof Identification) {
            Identification auth = (Identification) object;
            if(auth.getNameCover().isAdjusted()) {
                setName(auth.getName());
            }
        }
    }

    @Override
    public Identification replicate() {
        Identification value = new Identification();
        value.update(this);
        return value;
    }
}
