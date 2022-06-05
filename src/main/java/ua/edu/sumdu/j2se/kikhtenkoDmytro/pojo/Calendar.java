package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Amount;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.ObjectType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Calendar extends DatetimeRange {
    private final ObjectType<SortedMap<LocalDateTime, Set<Task>>> tasks;
    private final Amount amount;

    public SortedMap<LocalDateTime, Set<Task>> getTasks() {
        return tasks.getValue();
    }

    public Calendar() {
        tasks = new ObjectType<>("Calendar tasks", new TreeMap<>(), true);
        amount = new Amount("Calendar amount", null, true);
    }

    public void setTasks(SortedMap<LocalDateTime, Set<Task>> tasks) {
        this.tasks.setValue(tasks);
        this.tasks.setAdjusted(true);
    }

    public Integer getAmount() {
        return amount.getValue();
    }

    public void setAmount(Integer amount) {
        this.amount.setValue(amount);
        this.amount.setAdjusted(true);
    }

    @JsonIgnore
    public ObjectType<SortedMap<LocalDateTime,
            Set<Task>>> getTasksCover() {
        return tasks;
    }

    @JsonIgnore
    public Amount getAmountCover() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calendar) || !super.equals(o)) {
            return false;
        }
        Calendar calendar = (Calendar) o;
        return tasks.equals(calendar.tasks)
                && amount.equals(calendar.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tasks, amount);
    }

    @Override
    public String toString() {
        return "Calendar{" +
                super.toString() +
                ", tasks=" + tasks +
                ", amount=" + amount +
                '}';
    }

    @Override
    public Calendar replicate() {
        Calendar value = new Calendar();
        value.update(this);
        return value;
    }

    @Override
    public void update(Object object) {
        super.update(object);
        if(object instanceof Calendar) {
            Calendar value = (Calendar) object;
            if(value.getTasksCover().isAdjusted()) {
                setTasks(value.getTasks());
            }
            if(value.getAmountCover().isAdjusted()) {
                setAmount(value.getAmount());
            }
        }
    }
}
