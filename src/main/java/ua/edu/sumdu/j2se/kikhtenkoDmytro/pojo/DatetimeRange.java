package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedDatetime;

import java.time.LocalDateTime;
import java.util.Objects;

public class DatetimeRange implements Replicable {
    private final VerifiedDatetime start;
    private final VerifiedDatetime end;

    protected void checkTimeIntegrity(
            @Nullable LocalDateTime start,
            @Nullable LocalDateTime end
    ) {
        boolean isCorrect = true;
        if(start != null && end != null) {
            isCorrect = end.isAfter(start);
        }
        if(!isCorrect) {
            throw new IllegalArgumentException(
                    "End time must be higher than start"
            );
        }
    }

    public DatetimeRange() {
        start = new VerifiedDatetime("Start datetime", null, true) {
            @Override
            protected void checkMultiple(LocalDateTime value) {
                checkTimeIntegrity(value, getEnd());
            }
        };
        end = new VerifiedDatetime("End datetime", null, true) {
            @Override
            protected void checkMultiple(LocalDateTime value) {
                checkTimeIntegrity(getStart(), value);
            }
        };
    }

    public LocalDateTime getStart() {
        return start.getValue();
    }

    public void setStart(LocalDateTime start) {
        this.start.setValue(start);
        this.start.setAdjusted(true);
    }

    public LocalDateTime getEnd() {
        return end.getValue();
    }

    public void setEnd(LocalDateTime end) {
        this.end.setValue(end);
        this.end.setAdjusted(true);
    }

    @JsonIgnore
    public VerifiedDatetime getStartCover() {
        return start;
    }

    @JsonIgnore
    public VerifiedDatetime getEndCover() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatetimeRange)) {
            return false;
        }
        DatetimeRange that = (DatetimeRange) o;
        return start.equals(that.start) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "CalendarParams{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }


    @Override
    public DatetimeRange replicate() {
        DatetimeRange value = new DatetimeRange();
        value.update(this);
        return value;
    }

    @Override
    public void update(Object object) {
        if(object instanceof DatetimeRange) {
            DatetimeRange value = (DatetimeRange) object;
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
        }
    }
}
