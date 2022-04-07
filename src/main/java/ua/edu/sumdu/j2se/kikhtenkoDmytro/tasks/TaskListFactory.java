package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

public class TaskListFactory {
    static public AbstractTaskList createTaskList(ListTypes.types type) {
        if(type == ListTypes.types.ARRAY) {
            return new ArrayTaskList();
        } else {
            return new LinkedTaskList();
        }
    }
}
