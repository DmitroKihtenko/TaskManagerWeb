package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

import java.util.Iterator;

public abstract class AbstractTaskList implements Iterable<Task>, Cloneable {
    protected int taskAmount;
    protected static ListTypes.types type;

    public int size() {
        return taskAmount;
    }
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract Task getTask(int index);
    public AbstractTaskList incoming(int from, int to) {
        if(from > to) {
            throw new IllegalArgumentException(
                    "Invalid interval parameters!"
            );
        }

        int nextTaskTime;
        AbstractTaskList returnArr = TaskListFactory.createTaskList(type);

        for(Task task : this) {
            nextTaskTime = task.nextTimeAfter(from);

            if(nextTaskTime != -1 && nextTaskTime < to) {
                returnArr.add(task);
            }
        }
        return returnArr;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }
        if (this == otherObject) {
            return true;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        if(taskAmount != ((AbstractTaskList)otherObject).taskAmount) {
            return false;
        }

        Iterator otherIt = ((AbstractTaskList)otherObject).iterator();
        for(Task thisIt : this) {
            if(!thisIt.equals(otherIt.next())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskAmount;

        for(Task task : this) {
            result ^= task.hashCode();
        }

        if(type == ListTypes.types.ARRAY) {
            result = ~result;
        }

        return result;
    }

    @Override
    public String toString() {
        Iterator<Task> it = iterator();
        StringBuilder returnStr = new StringBuilder();
        int objNum = 0;
        returnStr.append(this.getClass().toString()).append("#");
        returnStr.append(taskAmount);

        while(it.hasNext()) {
            returnStr.append("#Object");
            returnStr.append(objNum);
            returnStr.append("#");
            returnStr.append(it.next().toString());
            objNum++;
        }

        return new String(returnStr);
    }
}
