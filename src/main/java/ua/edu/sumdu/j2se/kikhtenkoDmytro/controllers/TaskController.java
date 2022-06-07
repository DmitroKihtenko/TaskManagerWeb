package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.*;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.TaskService;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.CastAssertions;

import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;

    public TaskController(@NonNull TaskService taskService) {
        setTaskService(taskService);
    }

    @NonNull
    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(@NonNull TaskService service) {
        this.taskService = service;
    }

    @PreAuthorize("hasAuthority('TASKS')")
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResult<ArrayList<Task>> search(
            @RequestParam(value = "searchRegex",
                    defaultValue = "") String searchRegex,
            @RequestParam(value = "amount",
                    defaultValue = "50") String amount,
            @RequestParam(value = "from",
                    defaultValue = "0") String from)
            throws ServiceException {
        return taskService.search(
                searchRegex,
                CastAssertions.stringToInt(amount, "Search amount"),
                CastAssertions.stringToInt(from, "From value")
        );
    }

    @PreAuthorize("hasAuthority('TASKS')")
    @RequestMapping(method = RequestMethod.GET,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Task get(
            @PathVariable(value = "id") Integer id)
            throws ServiceException {
        return taskService.get(id, true);
    }

    @PreAuthorize("hasAuthority('TASKS')")
    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse delete(
            @PathVariable(value = "id") Integer id)
            throws ServiceException {
        taskService.delete(id);
        return new InfoResponse("Task successfully deleted");
    }

    @PreAuthorize("hasAuthority('TASKS')")
    @RequestMapping(method = RequestMethod.PATCH,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse update(
            @PathVariable Integer id,
            @RequestBody Task task)
            throws ServiceException {
        taskService.update(id, task);
        return new InfoResponse("Task successfully updated");
    }

    @PreAuthorize("hasAuthority('TASKS')")
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse add(@RequestBody Task task)
            throws ServiceException {
        taskService.add(task);
        return new InfoResponse("Task successfully added");
    }
}
