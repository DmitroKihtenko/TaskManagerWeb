package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Class Task for objects task.
 * @author Kikhtenko Dmytro
 * @version 1.0
 */
public class Task implements Externalizable {

    /**
     * Stores a title of task.
     */
    private String title;

    /**
     * Stores a state of task activity.
     */
    private boolean isActive;

    /**
     * If task is repeated ({@link Task#isPeriodical}) stores a time of task start.
     * If task is not repeated stores a time of task completion
     */
    private LocalDateTime start;

    /**
     * Stores the end time of non-repeated task completion.
     */
    private LocalDateTime end;

    /**
     * Stores an interval of task repeat.
     */
    private int interval;

    /**
     * Indicates whether the task is repeated.
     */
    private boolean isPeriodical;

    public Task() {
    }

    /**
     * Constructor for non repeated tasks (task activity defaults as false).
     * @param title title of the task
     * @param time time of completion of the task
     * @exception IllegalArgumentException if time parameter has negative value
     * @see Task#Task(String, LocalDateTime, LocalDateTime, int) constructor for repeated tasks
     */
    public Task(String title, LocalDateTime time) {
        setTitle(title);
        setTime(time);
    }

    /**
     * Constructor for repeated tasks (task activity defaults as false).
     * @param title title of the task
     * @param start start time of completion period
     * @param end end time of completion period
     * @param interval interval of task completion
     * @exception IllegalArgumentException if start time more or equal end time or interval is non-positive
     * @see Task#Task(String, LocalDateTime) constructor for non-repeated tasks
     */
    public Task(String title, LocalDateTime start,
                LocalDateTime end, int interval) {
        setTitle(title);
        setTime(start, end, interval);
    }

    /**
     * Getter for title of task.
     * @return title of task
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for title task.
     * @param title title of task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for completion time of task.
     * @return start time for repeated task and completion time for non-repeated
     */
    public LocalDateTime getTime() {
        return this.start;
    }

    /**
     * Setter for activity state of task.
     * @param active Sets the activity of task
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Getter for activity of task.
     * @return is the task active
     */
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Setter for completion time of non-repeated task (sets task as non-repeated).
     * @param time completion time for non-repeated task
     * @exception IllegalArgumentException if time parameter has negative value
     * @see Task#setTime(LocalDateTime, LocalDateTime, int) setter for repeated task
     */
    public void setTime(LocalDateTime time) {
        if(time == null) {
            throw new IllegalArgumentException(
                    "LocalDateTime parameter has null value!"
            );
        }

        this.start = time;

        if(this.isPeriodical) {
            this.isPeriodical = false;
        }
    }

    /**
     * Setter for time parameters of repeated task (sets task as repeated).
     * @param start start time of completion period
     * @param end end time of completion period
     * @param interval interval of task completion
     * @exception IllegalArgumentException if start time more or equal end time or interval is non-positive
     * @see Task#setTime(LocalDateTime) setter for non-repeated task
     */
    public void setTime(LocalDateTime start, LocalDateTime end,
                        int interval) {
        if(start == null || end == null || start.isAfter(end)
                || start.isEqual(end) || interval <= 0) {
            throw new IllegalArgumentException(
                    "Interval parameters error!"
            );
        }

        this.start = start;
        this.end = end;
        this.interval = interval;

        if(!this.isPeriodical) {
            this.isPeriodical = true;
        }
    }

    /**
     * Getter for start time of repeated task.
     * @return start time for repeated task and time of completion for non-repeated
     */
    public LocalDateTime getStartTime() {
        return this.start;
    }

    /** Getter for end time of repeated task.
     * @return end time for repeated task and time of completion for non-repeated
     */
    public LocalDateTime getEndTime() {
        if(this.isPeriodical) {
            return this.end;
        } else {
            return this.start;
        }
    }

    /** Getter for interval of repeated task.
     * @return interval for repeated task and 0 for non-repeated
     */
    public int getRepeatInterval() {
        if(this.isPeriodical) {
            return this.interval;
        } else {
            return 0;
        }
    }

    /**
     * Getter for repeat state of task.
     * @return repeat state of task
     */
    public boolean isRepeated() {
        return this.isPeriodical;
    }

    /**
     * Method that returns next time of task completion after given time.
     * @param current the time relative to which you want to find the completion time
     * @exception IllegalArgumentException if time parameter has negative value
     * @return time of next task completion after current time. If task is not active returns 0. If task will not run after current time returns 0
     */
    public LocalDateTime nextTimeAfter(LocalDateTime current) {
        if(current == null) {
            throw new IllegalArgumentException(
                    "LocalDateTime object has null value!"
            );
        }
        if(!this.isActive) {
            return null;
        }

        if(this.isPeriodical) {
            LocalDateTime nextTime = this.start;

            while(!current.isBefore(nextTime)) {
                if(nextTime.plusSeconds(this.interval).isAfter(this.end)) {
                    return null;
                } else {
                    nextTime = nextTime.plusSeconds(this.interval);
                }
            }

            return nextTime;
        } else {
            if(current.isBefore(this.start)) {
                return this.start;
            } else {
                return null;
            }
        }
    }

    /**
     * Equals method for Task class objects.
     * @param otherObject task class object for comparison
     * @return true if tasks have same field values, else return false
     */
    @Override
    public boolean equals(Object otherObject) {
        if(otherObject == null) {
            return false;
        }
        if(this == otherObject) {
            return true;
        }
        if(getClass() != otherObject.getClass()) {
            return false;
        }
        return title.equals(((Task) otherObject).title) &&
                start == ((Task) otherObject).start &&
                end == ((Task) otherObject).end &&
                interval == ((Task) otherObject).interval &&
                isActive == ((Task) otherObject).isActive &&
                isPeriodical == ((Task) otherObject).isPeriodical;
    }

    /**
     * Hash code method for Task class objects.
     * @return Unique hash code for Task class object (there may be collisions)
     */
    @Override
    public int hashCode() {
        int result = 0;

        result ^= title.hashCode();
        result ^= start.hashCode();
        result ^= end.hashCode();
        result += interval;
        if(isActive) {
            result >>= 3;
        } else {
            result >>= 5;
        }
        if(isPeriodical) {
            result <<= 7;
        } else {
            result <<= 11;
        }

        return result;
    }

    /**
     * To string method for Task class objects.
     * @return string that consists of class name and all object fields through the symbol #
     */
    @Override
    public String toString() {
        return "Task.class#" + title + "#" + start + "#" + end
                + "#" + interval + "#" + isActive + "#" + isPeriodical;
    }

    /**
     * Clone method for the Task class objects.
     * @return clone of the object (pointer on this object)
     * @throws CloneNotSupportedException if class is not implements Cloneable() interface
     */
    @Override
    public Task clone() throws CloneNotSupportedException {
        Task result;
        if(isPeriodical) {
            result = new Task(title, start, end, interval);
        } else {
            result = new Task(title, start);
        }
        result.isActive = isActive;
        return result;
    }

    /**
     * Overrode method for control Task objects serialization
     * @param out stream where objects are being writing
     * @exception IOException if something wrong with writing into stream
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(title.length());
        out.writeObject(title);
        if(isActive) {
            out.writeInt(1);
        } else {
            out.writeInt(0);
        }
        out.writeInt(getRepeatInterval());
        out.writeLong(start.toEpochSecond(ZoneOffset.UTC));
        if(isRepeated()) {
            out.writeLong(end.toEpochSecond(ZoneOffset.UTC));
        }
    }

    /**
     * Overrode method for control Task objects deserialization
     * @param in stream from which objects are being reading
     * @exception IOException if something wrong with reading from stream
     * @exception ClassNotFoundException if something wrong with deserialization String field title
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readInt();
        title = (String)in.readObject();
        int activity = in.readInt();
        isActive = activity == 1;
        interval = in.readInt();
        start = LocalDateTime.ofEpochSecond(in.readLong(),
                0, ZoneOffset.UTC);
        if(interval == 1) {
            isPeriodical = true;
            end = LocalDateTime.ofEpochSecond(in.readLong(),
                    0, ZoneOffset.UTC);
        } else {
            isPeriodical = false;
        }
    }
}
