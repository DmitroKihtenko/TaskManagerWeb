package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

/**
 * Class Task for objects task.
 * @author Kikhtenko Dmytro
 * @version 1.0
 */
public class Task {

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
    private int start;

    /**
     * Stores the end time of non-repeated task completion.
     */
    private int end;

    /**
     * Stores an interval of task repeat.
     */
    private int interval;

    /**
     * Indicates whether the task is repeated.
     */
    private boolean isPeriodical;

    /**
     * Constructor for non repeated tasks (task activity defaults as false).
     * @param title title of the task
     * @param time time of completion of the task
     * @exception IllegalArgumentException if time parameter has negative value
     * @see Task#Task(String, int, int, int) constructor for repeated tasks
     */
    public Task(String title, int time) {
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
     * @see Task#Task(String, int) constructor for non-repeated tasks
     */
    public Task(String title, int start, int end, int interval) {
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
    public int getTime() {
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
     * @see Task#setTime(int, int, int) setter for repeated task
     */
    public void setTime(int time) {
        if(time < 0) {
            throw new IllegalArgumentException(
                    "Negative time parameter!"
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
     * @see Task#setTime(int) setter for non-repeated task
     */
    public void setTime(int start, int end, int interval) {
        if(start >= end || interval <= 0) {
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
    public int getStartTime() {
        return this.start;
    }

    /** Getter for end time of repeated task.
     * @return end time for repeated task and time of completion for non-repeated
     */
    public int getEndTime() {
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
    public int nextTimeAfter(int current) {
        if(current < 0) {
            throw new IllegalArgumentException(
                    "Negative time parameter!"
            );
        }

        if(!this.isActive) {
            return -1;
        }

        if(this.isPeriodical) {
            int nextTime = this.start;

            while(current >= nextTime) {
                if(nextTime + this.interval <= this.end) {
                    nextTime += this.interval;
                } else {
                    return -1;
                }
            }

            return nextTime;
        } else {
            if(current < this.start) {
                return this.start;
            } else {
                return -1;
            }
        }
    }
}
