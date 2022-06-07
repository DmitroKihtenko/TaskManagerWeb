package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task extends IdHolder implements Replicable {
    public static final Duration MIN_INTERVAL = Duration.ofMinutes(5);
    private final Title title;
    private final BooleanType isActive;
    private final VerifiedDatetime start;
    private final VerifiedDatetime end;
    private final VerifiedDuration interval;

    public static boolean checkTimeIntegrity(
            @Nullable LocalDateTime start,
            @Nullable LocalDateTime end,
            boolean withError) {
        boolean isCorrect = true;
        if(start != null && end != null) {
            isCorrect = end.isAfter(start.plus(MIN_INTERVAL));
        }
        if(withError && !isCorrect) {
            throw new IllegalArgumentException(
                    "Difference between start and end date " +
                            "can not be less than " +
                            MIN_INTERVAL.toMinutes() + " minutes"
            );
        }
        return isCorrect;
    }

    public static void checkTimeIntegrity(
            @Nullable LocalDateTime start,
            @Nullable LocalDateTime end) {
        checkTimeIntegrity(start, end, true);
    }

    public Task() {
        title = new Title("Task title", null, true);
        isActive = new BooleanType("Task is active", true, true);
        start = new VerifiedDatetime("Task start", LocalDateTime.now(), true) {
            @Override
            protected void checkMultiple(LocalDateTime value) {
                checkTimeIntegrity(value, end.getValue());
            }
        };
        end = new VerifiedDatetime("Task end", null, false) {
            @Override
            protected void checkMultiple(LocalDateTime value) {
                checkTimeIntegrity(start.getValue(), value);
            }
        };
        interval = new VerifiedDuration("Task interval", null, false) {
            @Override
            public void checkValue(Duration value) {
                super.checkValue(value);
                if(value != null && value.compareTo(MIN_INTERVAL) < 0) {
                    throw new IllegalArgumentException("To small " +
                            "task interval. Minimal value " +
                            MIN_INTERVAL.toMinutes() + " minutes");
                }
            }
        };
    }

    public String getTitle() {
        return title.getValue();
    }

    public void setTitle(String title) {
        this.title.setValue(title);
        this.title.setAdjusted(true);
    }

    public Boolean isActive() {
        return isActive.getValue();
    }

    public void setActive(Boolean active) {
        isActive.setValue(active);
        isActive.setAdjusted(true);
    }

    public LocalDateTime getStart() {
        return start.getValue();
    }

    public void setStart(@NonNull LocalDateTime start) {
        this.start.setValue(start);
        this.start.setAdjusted(true);
    }

    public LocalDateTime getEnd() {
        return end.getValue();
    }

    public void setEnd(LocalDateTime end) {
        this.end.setValue(end);
        this.end.setAdjusted(true);
        if(interval == null) {
            setInterval(MIN_INTERVAL);
        }
    }

    public Duration getInterval() {
        return interval.getValue();
    }

    public void setInterval(Duration interval) {
        this.interval.setValue(interval);
        this.interval.setAdjusted(true);
    }

    @JsonIgnore
    public boolean isRepeated() {
        return interval.getValue() != null && end.getValue() != null;
    }

    @JsonIgnore
    public Title getTitleCover() {
        return this.title;
    }

    @JsonIgnore
    public BooleanType getIsActiveCover() {
        return this.isActive;
    }

    @JsonIgnore
    public VerifiedDatetime getStartCover() {
        return this.start;
    }

    @JsonIgnore
    public VerifiedDatetime getEndCover() {
        return this.end;
    }

    @JsonIgnore
    public VerifiedDuration getIntervalCover() {
        return this.interval;
    }

    public LocalDateTime nextTimeAfter(
            @NonNull LocalDateTime current) {
        if(!isActive() || getStart() == null) {
            return null;
        }

        if(isRepeated()) {
            LocalDateTime nextTime = getStart();

            while(!current.isBefore(nextTime)) {
                if(nextTime.plus(getInterval()).isAfter(getEnd())) {
                    return null;
                } else {
                    nextTime = nextTime.plus(getInterval());
                }
            }

            return nextTime;
        } else {
            if(current.isBefore(getStart())) {
                return getStart();
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return title.equals(task.title) &&
                isActive.equals(task.isActive)
                && start.equals(task.start) &&
                end.equals(task.end) &&
                interval.equals(task.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, isActive, start, end, interval);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title=" + title +
                ", isActive=" + isActive +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                '}';
    }

    @Override
    public Task replicate() {
        Task value = new Task();
        value.update(this);
        return value;
    }

    @Override
    public void update(Object object) {
        if(object instanceof Task) {
            Task value = (Task) object;
            super.update(value);
            if(value.getTitleCover().isAdjusted()) {
                setTitle(value.getTitle());
            }
            if(value.getIsActiveCover().isAdjusted()) {
                setActive(value.isActive());
            }
            if(value.getStartCover().isAdjusted() &&
                    value.getEndCover().isAdjusted()) {
                VerifiedCustomType.setValues(
                        Lists.newArrayList(start, end),
                        Lists.newArrayList(
                                value.getStart(),
                                value.getEnd()
                        )
                );
            } else if(value.getStartCover().isAdjusted()) {
                setStart(value.getStart());
            } else if(value.getEndCover().isAdjusted()) {
                setEnd(value.getEnd());
            }
            if(value.getIntervalCover().isAdjusted()) {
                setInterval(value.getInterval());
            }
        }
    }
}
