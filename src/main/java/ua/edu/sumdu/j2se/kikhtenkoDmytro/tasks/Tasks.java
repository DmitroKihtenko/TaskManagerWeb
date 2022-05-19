package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

import java.time.LocalDateTime;
import java.util.*;

public class Tasks {
    public static Iterable<Task> incoming(
            Iterable<Task> tasks, LocalDateTime from, LocalDateTime to) {
        if(from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "Invalid interval parameters!"
            );
        }

        LinkedList<Task> retTasks = new LinkedList<Task>();
        LocalDateTime nextTime;

        for(Task currentTask : tasks) {
            nextTime = currentTask.nextTimeAfter(from);
            if(nextTime != null && !nextTime.isAfter(to)) {
                retTasks.add(currentTask);
            }
        }
        return retTasks;
    }

    public static SortedMap<LocalDateTime, Set<Task>> calendar(
                    Iterable<Task> tasks,
                    LocalDateTime start,
                    LocalDateTime end) {
        if(start == null || end == null) {
            throw new IllegalArgumentException(
                    "LocalDateTime object has null value!"
            );
        }

        TreeMap<LocalDateTime, Set<Task>> calendar = new TreeMap<>();
        LocalDateTime beginTime;
        Set<Task> setPtr;

        tasks = incoming(tasks, start, end);

        for(Task tempTask : tasks) {
            beginTime = start;
            while (true) {
                beginTime = tempTask.nextTimeAfter(beginTime);

                if(beginTime == null || beginTime.isAfter(end)) {
                    break;
                }

                if(calendar.containsKey(beginTime)) {
                    setPtr = calendar.get(beginTime);
                } else {
                    setPtr = new HashSet<>();
                    calendar.put(beginTime, setPtr);
                }
                setPtr.add(tempTask);
            }
        }

        return calendar;
    }
}
