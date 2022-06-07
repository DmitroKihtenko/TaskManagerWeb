package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

import org.springframework.lang.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public abstract class VerifiedCustomType<T> implements CustomType<T> {
    private T value;
    private boolean mandatory;
    private boolean adjusted;
    private String parameter;

    public static <T> void setValues(
            Iterable<VerifiedCustomType<T>> types,
            Iterable<T> values
    ) {
        VerifiedCustomType<T> type;
        T value;
        Iterator<VerifiedCustomType<T>> typesIter = types.iterator();
        Iterator<T> valuesIterator;
        LinkedList<T> oldValues = new LinkedList<>();
        while (typesIter.hasNext()) {
            oldValues.add(typesIter.next().getValue());
        }
        try {
            typesIter = types.iterator();
            valuesIterator = values.iterator();
            while(typesIter.hasNext() && valuesIterator.hasNext()) {
                type = typesIter.next();
                value = valuesIterator.next();
                type.checkValue(value);
            }
            typesIter = types.iterator();
            valuesIterator = values.iterator();
            while(typesIter.hasNext() && valuesIterator.hasNext()) {
                type = typesIter.next();
                value = valuesIterator.next();
                type.value = value;
            }
            typesIter = types.iterator();
            if(typesIter.hasNext()) {
                type = typesIter.next();
                type.checkMultiple(type.getValue());
            }
        } catch (Exception e) {
            typesIter = types.iterator();
            valuesIterator = oldValues.iterator();
            while(typesIter.hasNext() && valuesIterator.hasNext()) {
                type = typesIter.next();
                type.setValue(valuesIterator.next());
            }
            throw e;
        }
    }

    public VerifiedCustomType(@NonNull String parameter, T initial, boolean mandatory) {
        setParameter(parameter);
        value = initial;
        setMandatory(mandatory);
    }

    protected void checkValue(T value) {
        if(mandatory && value == null) {
            throw new IllegalArgumentException(
                    parameter + " value is mandatory");
        }
    }

    protected void checkMultiple(T value) {}

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        checkValue(value);
        checkMultiple(value);
        this.value = value;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isAdjusted() {
        return adjusted;
    }

    public void setAdjusted(boolean adjusted) {
        this.adjusted = adjusted;
    }

    @NonNull
    public String getParameter() {
        return parameter;
    }

    public void setParameter(@NonNull String parameter) {
        this.parameter = parameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!getClass().isInstance(o)) {
            return false;
        }
        VerifiedCustomType<?> that = (VerifiedCustomType<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "value=" + value +
                '}';
    }
}
