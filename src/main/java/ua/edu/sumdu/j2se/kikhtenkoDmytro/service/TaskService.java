package ua.edu.sumdu.j2se.kikhtenkoDmytro.service;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.EntityQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.TaskQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.SearchResult;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Task;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.ServiceExceptionWrapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Primary
@Scope(
        scopeName = WebApplicationContext.SCOPE_REQUEST,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
public class TaskService extends EntityService<Task> {
    private UserService userService;

    public TaskService(
            TaskQueries entityQueries,
            UserService userService,
            ServiceExceptionWrapper wrapper) {
        super(entityQueries, wrapper);
        setUserService(userService);
    }

    @Resource(type = TaskQueries.class)
    @Override
    public void setEntityQueries(EntityQueries<Task> entityQueries) {
        super.setEntityQueries(entityQueries);
    }

    @NonNull
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(@NonNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public Task get(
            int key,
            boolean errorOnNotFound
    ) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        return super.get(key, errorOnNotFound);
    }

    public ArrayList<Task> get(
            @NonNull LocalDateTime from,
            @NonNull LocalDateTime to) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        TaskQueries entityQueries = (TaskQueries) getEntityQueries();
        ArrayList<Task> result = new ArrayList<>();
        try {
            entityQueries.getProvider().connect();
            entityQueries.setExtraParams(
                    userService.getCurrentAuthentication().getId());
            int amount = entityQueries.amountByDate(from, to);
            checkLimit(amount);
            result = entityQueries.getByDate(from, to);
        } catch (Exception e) {
            getWrapper().wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
        return result;
    }

    @Override
    public SearchResult<ArrayList<Task>> search(
            String regex,
            int amount,
            int from
    ) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        return super.search(regex, amount, from);
    }

    @Override
    public void update(
            int key,
            Task entity
    ) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        super.update(key, entity);
    }

    @Override
    public void add(Task entity) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        super.add(entity);
    }

    @Override
    public void delete(int key) throws ServiceException {
        getEntityQueries().setExtraParams(
                userService.getCurrentAuthentication().getId());
        super.delete(key);
    }
}
