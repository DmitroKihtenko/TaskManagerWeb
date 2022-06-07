package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types;

public interface CustomType<T> {
    void setValue(T value);
    T getValue();
}
