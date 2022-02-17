package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

public class ArrayTaskList {
    /**
     * Amount of added or deleted tasks after which the array memory is changed.
     */
    private final static int RESIZE_INTERVAL = 5;

    private int taskAmount;
    private Task[] taskArr;

    public ArrayTaskList() {
        taskArr = new Task[RESIZE_INTERVAL];
    }

    public void add(Task task) {
        if(taskArr.length == taskAmount) {
            Task[] tempArr = new Task[taskAmount + RESIZE_INTERVAL];

            System.arraycopy(taskArr, 0, tempArr, 0, taskAmount);
            taskArr = tempArr;
        }

        taskArr[taskAmount] = task;
        taskAmount++;
    }

    public boolean remove(Task task) {
        boolean searchStatus = false;
        int delIndex;

        for(delIndex = 0; delIndex < taskAmount; delIndex++) {
            if(taskArr[delIndex].equals(task)) {
                searchStatus = true;
                break;
            }
        }

        if(!searchStatus) {
            return false;
        }

        taskArr[delIndex] = null;
        taskAmount--;

        if(delIndex != taskAmount) {
            System.arraycopy(taskArr, delIndex + 1, taskArr, delIndex,
                    taskAmount - delIndex);
        }

        if(taskArr.length - RESIZE_INTERVAL == taskAmount &&
                taskAmount != 0) {
            Task[] tempArr = new Task[taskAmount];

            System.arraycopy(taskArr, 0, tempArr, 0, taskAmount);
            taskArr = tempArr;
        }

        return true;
    }

    public int size() {
        return taskAmount;
    }

    /**
     * After using remove task method indexes of specific objects can change.
     */
    public Task getTask(int index) {
        return taskArr[index];
    }

    public ArrayTaskList incoming(int from, int to) {
        int nextTaskTime;
        ArrayTaskList returnArr = new ArrayTaskList();

        for(short index = 0; index < taskAmount; index++) {
            nextTaskTime = taskArr[index].nextTimeAfter(from);

            if(nextTaskTime != -1 && nextTaskTime < to) {
                returnArr.add(taskArr[index]);
            }
        }

        return returnArr;
    }
}
