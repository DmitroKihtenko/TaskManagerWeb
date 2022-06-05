package ua.edu.sumdu.j2se.kikhtenkoDmytro.service;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Calendar;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.DatetimeRange;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Task;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Primary
@Scope(
        scopeName = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class CalendarService {
    private TaskService taskService;

    public CalendarService(@NonNull TaskService taskService) {
        setTaskService(taskService);
    }

    @NonNull
    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(@NonNull TaskService taskService) {
        this.taskService = taskService;
    }

    public LinkedList<Task> incomingTasks(@NonNull DatetimeRange params)
            throws ServiceException {
        ArrayList<Task> tasks = taskService.get(
                params.getStart(), params.getEnd());

        LinkedList<Task> retTasks = new LinkedList<>();
        LocalDateTime nextTime;

        for(Task currentTask : tasks) {
            nextTime = currentTask.nextTimeAfter(params.getStart());

            if(nextTime != null && !nextTime.isAfter(params.getEnd())) {
                retTasks.add(currentTask);
            }
        }
        return retTasks;
    }

    public Calendar get(@NonNull DatetimeRange params) throws ServiceException {
        TreeMap<LocalDateTime, Set<Task>> calendar = new TreeMap<>();
        LocalDateTime beginTime;
        Set<Task> setPtr;

        LinkedList<Task> tasks = incomingTasks(params);

        int amount = 0;
        for(Task tempTask : tasks) {
            beginTime = params.getStart();
            while (true) {
                beginTime = tempTask.nextTimeAfter(beginTime);
                if(beginTime == null || beginTime.isAfter(params.getEnd())) {
                    break;
                }
                if(calendar.containsKey(beginTime)) {
                    setPtr = calendar.get(beginTime);
                } else {
                    setPtr = new HashSet<>();
                    calendar.put(beginTime, setPtr);
                }
                setPtr.add(tempTask);
                amount++;
            }
        }

        Calendar result = new Calendar();
        result.update(params);
        result.setAmount(amount);
        result.setTasks(calendar);

        return result;
    }
}
