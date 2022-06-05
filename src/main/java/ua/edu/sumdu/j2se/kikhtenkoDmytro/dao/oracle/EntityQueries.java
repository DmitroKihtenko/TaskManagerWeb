package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryLoader;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryProvider;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.TableQueries;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public abstract class EntityQueries<E> extends TableQueries {
    private List<Object> extraParams;
    public EntityQueries(QueryProvider provider, QueryLoader queryLoader) {
        super(provider, queryLoader);
        extraParams = new ArrayList<>();
    }

    protected void setCriteria(@NonNull AtomicInteger pos,
                               List<Object> params) throws SQLException {}

    protected abstract int getSearchColumns();

    @NonNull
    public List<Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(@NonNull List<Object> extraParams) {
        this.extraParams = extraParams;
    }

    public void setExtraParams(@NonNull Object... params) {
        extraParams = Arrays.asList(params);
    }

    public abstract E fromQuery(
            @NonNull ResultSet set,
            @NonNull AtomicInteger pos
    ) throws Exception;

    public E fromQuery(@NonNull ResultSet set) throws Exception {
        return fromQuery(set, new AtomicInteger(1));
    }

    public abstract void toQuery(
            @NonNull E entity,
            @NonNull QueryProvider provider,
            @NonNull AtomicInteger pos) throws Exception;

    public void toQuery(
            @NonNull E entity,
            @NonNull QueryProvider provider
    ) throws Exception {
        toQuery(entity, provider, new AtomicInteger(1));
    }

    @Nullable
    public E get(int key) throws Exception {
        getProvider().prepare(getQueryLoader().load("get"));
        AtomicInteger pos = new AtomicInteger(1);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                key, JDBCType.INTEGER);
        setCriteria(pos, getExtraParams());
        ResultSet set = getProvider().executePrepared();
        if(set.next()) {
            return fromQuery(set);
        } else {
            return null;
        }
    }

    @NonNull
    public ArrayList<E> search(
            int amount,
            int from,
            @NonNull String regex) throws Exception {
        AtomicInteger pos = new AtomicInteger(1);
        QueryProvider prov = getProvider();
        if(regex.equals("")) {
            prov.prepare(getQueryLoader().load("get-all"));
        } else {
            prov.prepare(getQueryLoader().load("search"));
            for(int column = 0; column < getSearchColumns(); column++) {
                prov.setPrepareArguments(pos.getAndIncrement(), regex,
                        JDBCType.NVARCHAR);
            }
        }
        setCriteria(pos, getExtraParams());
        prov.setPrepareArguments(pos.getAndIncrement(),
                from + amount, JDBCType.INTEGER);
        prov.setPrepareArguments(pos.getAndIncrement(),
                from, JDBCType.INTEGER);
        ResultSet set = prov.executePrepared();
        ArrayList<E> entities = new ArrayList<>(
                set.getFetchSize());
        while(set.next()) {
            entities.add(fromQuery(set));
        }
        return entities;
    }

    @Nullable
    public void delete(int key) throws Exception {
        getProvider().prepare(getQueryLoader().load("delete"));
        AtomicInteger pos = new AtomicInteger(1);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                key, JDBCType.INTEGER);
        setCriteria(pos, getExtraParams());
        getProvider().executePrepared();
    }

    public void insert(@NonNull E entity) throws Exception {
        getProvider().prepare(getQueryLoader().load("insert"));
        AtomicInteger pos = new AtomicInteger(1);
        toQuery(entity, getProvider(), pos);
        getProvider().executePrepared();
    }

    public void update(
            int key,
            @NonNull E entity
    ) throws Exception {
        getProvider().prepare(getQueryLoader().load("update"));
        AtomicInteger pos = new AtomicInteger(1);
        toQuery(entity, getProvider(), pos);
        getProvider().setPrepareArguments(pos.getAndIncrement(), key,
                JDBCType.INTEGER);
        setCriteria(pos, getExtraParams());
        getProvider().executePrepared();
    }

    public int size(@NonNull String regex) throws Exception {
        AtomicInteger pos = new AtomicInteger(1);
        QueryProvider prov = getProvider();
        if(regex.equals("")) {
            prov.prepare(getQueryLoader().load("amount"));
        } else {
            prov.prepare(getQueryLoader().load("search-amount"));
            for(int column = 0; column < getSearchColumns();
                column++) {
                prov.setPrepareArguments(pos.getAndIncrement(), regex,
                        JDBCType.NVARCHAR);
            }
        }
        setCriteria(pos, getExtraParams());
        ResultSet set = getProvider().executePrepared();
        set.next();
        return set.getInt(1);
    }
}
