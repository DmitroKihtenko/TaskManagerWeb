package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

public abstract class AbstractTaskList {
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
        Task currentTask;
        AbstractTaskList returnArr = TaskListFactory.createTaskList(type);

        for(short index = 0; index < taskAmount; index++) {
            currentTask = getTask(index);
            nextTaskTime = currentTask.nextTimeAfter(from);

            if(nextTaskTime != -1 && nextTaskTime < to) {
                returnArr.add(currentTask);
            }
        }

        return returnArr;
    }
}
