package ua.edu.sumdu.j2se.kikhtenkoDmytro.service;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle.EntityQueries;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.SearchResult;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Updatable;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.NonZeroAmount;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.ServiceExceptionWrapper;

import java.util.ArrayList;

public abstract class EntityService<E extends Updatable> {
    private final NonZeroAmount entitiesLimit;
    private EntityQueries<E> entityQueries;
    private ServiceExceptionWrapper wrapper;

    public EntityService(
            @NonNull EntityQueries<E> entityQueries,
            @NonNull ServiceExceptionWrapper wrapper) {
        setEntityQueries(entityQueries);
        setWrapper(wrapper);
        entitiesLimit = new NonZeroAmount("Entities limit", 50, true);
    }

    public Integer getEntitiesLimit() {
        return entitiesLimit.getValue();
    }

    public void setEntitiesLimit(Integer entitiesLimit) {
        this.entitiesLimit.setValue(entitiesLimit);
        this.entitiesLimit.setAdjusted(true);
    }

    @NonNull
    public EntityQueries<E> getEntityQueries() {
        return entityQueries;
    }

    public void setEntityQueries(@NonNull EntityQueries<E>
                                         entityQueries) {
        this.entityQueries = entityQueries;
    }

    public ServiceExceptionWrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(@NonNull ServiceExceptionWrapper
                                   wrapper) {
        this.wrapper = wrapper;
    }

    protected void checkLimit(int limit) throws ServiceException {
        if(limit > getEntitiesLimit()) {
            throw new ServiceException(
                    "Too many entities to retrieve. " +
                            "Maximal value " + getEntitiesLimit(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Nullable
    public E get(int key, boolean errorOnNotFound)
            throws ServiceException {
        E entity = null;
        try {
            entityQueries.getProvider().connect();
            entity = entityQueries.get(key);
        } catch (Exception e) {
            wrapper.wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
        if(errorOnNotFound && entity == null) {
            throw new ServiceException(
                    "Entity with key '" + key + "' not found"
            );
        }
        return entity;
    }

    @Nullable
    public E get(int key) throws ServiceException {
        return get(key, false);
    }

    @NonNull
    public SearchResult<ArrayList<E>> search(
            @NonNull String regex,
            int amount, int from
    ) throws ServiceException {
        SearchResult<ArrayList<E>> result =
                new SearchResult<>();
        try {
            entityQueries.getProvider().connect();
            int size = entityQueries.size(regex);
            checkLimit(size);
            result.setTotal(size);
            result.setFrom(from);
            ArrayList<E> entities = entityQueries.search(
                    amount, from, regex);
            result.setCurrent(entities.size());
            result.setResult(entities);
        } catch (Exception e) {
            wrapper.wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
        return result;
    }

    public void add(@NonNull E entity)
            throws ServiceException {
        try {
            entityQueries.getProvider().connect();
            entityQueries.insert(entity);
        } catch (Exception e) {
            wrapper.wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
    }

    public void update(int key, @NonNull E entity)
            throws ServiceException {
        try {
            entityQueries.getProvider().connect();
            E current = entityQueries.get(key);
            if(current == null) {
                throw new ServiceException(
                        "Entity with key '" + key + "' not found"
                );
            }
            current.update(entity);
            entityQueries.update(key, current);
        } catch (Exception e) {
            wrapper.wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
    }

    public void delete(int key) throws ServiceException {
        try {
            entityQueries.getProvider().connect();
            entityQueries.delete(key);
        } catch (Exception e) {
            wrapper.wrap(e);
        } finally {
            entityQueries.getProvider().disconnect();
        }
    }
}
