package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryLoader;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryProvider;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Task;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.CastAssertions;

import java.io.*;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Scope(scopeName = "prototype")
public class TaskQueries extends EntityQueries<Task> {
    public TaskQueries(
            @NonNull QueryProvider provider,
            @Qualifier("taskQueryLoader") QueryLoader queryLoader) {
        super(provider, queryLoader);
    }
    @Override
    protected void setCriteria(
            AtomicInteger pos,
            List<Object> params
    ) throws SQLException {
        if(params.size() >= 1) {
            getProvider().setPrepareArguments(pos.getAndIncrement(),
                    params.get(0), JDBCType.INTEGER);
        }
    }

    @Override
    protected int getSearchColumns() {
        return 1;
    }

    @Override
    public Task fromQuery(
            ResultSet set,
            AtomicInteger pos) throws Exception {
        Task task = new Task();
        task.setId(set.getInt(pos.getAndIncrement()));
        task.setTitle(set.getString(pos.getAndIncrement()));
        task.setStart(set.getTimestamp(pos.getAndIncrement()).
                toLocalDateTime());
        Timestamp timestamp = set.getTimestamp(pos.getAndIncrement());
        if(timestamp != null) {
            task.setEnd(timestamp.toLocalDateTime());
        }
        long interval = set.getLong(pos.getAndIncrement());
        if(set.wasNull()) {
            task.setInterval(null);
        } else {
            task.setInterval(Duration.ofSeconds(interval));
        }
        task.setActive(CastAssertions.charToBool(
                set.getString(pos.getAndIncrement()),
                "Task is enabled"));
        return task;
    }

    @Override
    public void toQuery(
            Task entity,
            QueryProvider provider,
            AtomicInteger pos) throws Exception {
        provider.setPrepareArguments(pos.getAndIncrement(),
                entity.getTitle(), JDBCType.NVARCHAR);
        provider.setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(entity.getStart()), JDBCType.DATE);
        if(entity.getEnd() != null) {
            provider.setPrepareArguments(pos.getAndIncrement(),
                    Timestamp.valueOf(entity.getEnd()), JDBCType.DATE);
        } else {
            provider.setPrepareArguments(pos.getAndIncrement(),
                    null, JDBCType.NULL);
        }
        if(entity.getInterval() != null) {
            provider.setPrepareArguments(pos.getAndIncrement(),
                    entity.getInterval().getSeconds(),
                    JDBCType.NUMERIC);
        } else {
            provider.setPrepareArguments(pos.getAndIncrement(),
                    null, JDBCType.NULL);
        }
        String enabled = null;
        if(entity.isActive() != null) {
            enabled = CastAssertions.boolToChar(entity.isActive());
        }
        provider.setPrepareArguments(pos.getAndIncrement(),
                enabled, JDBCType.CHAR);
        setCriteria(pos, getExtraParams());
    }

    public ArrayList<Task> getByDate(
            @NonNull LocalDateTime from,
            @NonNull LocalDateTime to
    ) throws Exception {
        getProvider().prepare(getQueryLoader().load("get-by-date"));
        AtomicInteger pos = new AtomicInteger(1);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        setCriteria(pos, getExtraParams());
        ResultSet set = getProvider().executePrepared();
        ArrayList<Task> result = new ArrayList<>(set.getFetchSize());
        while(set.next()) {
            result.add(fromQuery(set));
        }
        return result;
    }

    public int amountByDate(
            @NonNull LocalDateTime from,
            @NonNull LocalDateTime to
    ) throws Exception {
        getProvider().prepare(getQueryLoader().load("amount-by-date"));
        AtomicInteger pos = new AtomicInteger(1);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(from), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        getProvider().setPrepareArguments(pos.getAndIncrement(),
                Timestamp.valueOf(to), JDBCType.TIMESTAMP);
        setCriteria(pos, getExtraParams());
        ResultSet set = getProvider().executePrepared();
        set.next();
        return set.getInt(1);
    }

    protected Set<Authority> bytesToAuthorities(@NonNull byte[] bytes)
            throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {
            return (Set<Authority>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error processing user metadata. "
                    + e.getMessage());
        }
    }

    protected byte[] authoritiesToBytes(Set<Authority> authorities) throws IOException {
        ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bytesStream)) {
            oos.writeObject(authorities);
            return bytesStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Error processing user metadata. "
                    + e.getMessage());
        }
    }
}
