package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Identifier;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

public class IdHolder implements Replicable {
    private final VerifiedCustomType<Integer> id;

    public IdHolder() {
        id = new Identifier("Entity id", null,true);
    }

    public IdHolder(int id) {
        this();
        setId(id);
    }

    public void setId(Integer id) {
        this.id.setValue(id);
        this.id.setAdjusted(true);
    }

    public Integer getId() {
        return id.getValue();
    }

    @JsonIgnore
    public VerifiedCustomType<Integer> getIdCover() {
        return id;
    }

    @Override
    public String toString() {
        return "IdHolder{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdHolder)) {
            return false;
        }
        IdHolder idHolder = (IdHolder) o;
        return id == idHolder.id;
    }

    @Override
    public int hashCode() {
        return id.getValue();
    }

    @Override
    public void update(Object object) {
        if(object instanceof IdHolder) {
            IdHolder value = (IdHolder) object;
            if(value.getIdCover().isAdjusted()) {
                setId(value.getId());
            }
        }
    }

    @Override
    public IdHolder replicate() {
        IdHolder object = new IdHolder();
        object.update(this);
        return object;
    }
}
