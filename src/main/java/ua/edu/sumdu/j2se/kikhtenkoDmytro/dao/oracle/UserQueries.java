package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryLoader;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.dao.QueryProvider;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.UserData;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.CastAssertions;

import java.io.*;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Scope(scopeName = "prototype")
public class UserQueries extends EntityQueries<UserData> {

    @Autowired
    public UserQueries(@NonNull QueryProvider queryProvider,
                       @Qualifier("userQueryLoader") QueryLoader
                               queryLoader) {
        super(queryProvider, queryLoader);
    }

    @Override
    protected int getSearchColumns() {
        return 1;
    }

    @NonNull
    public UserData fromQuery(
            @NonNull ResultSet set,
            @NonNull AtomicInteger pos,
            boolean withPassword
    ) throws SQLException, IOException {
        UserData newUserData = new UserData();
        newUserData.setId(set.getInt(pos.getAndIncrement()));
        newUserData.setName(set.getString(pos.getAndIncrement()));
        if(withPassword) {
            newUserData.setPassword(set.getString(pos.getAndIncrement()));
        }
        newUserData.setEnabled(CastAssertions.charToBool(
                set.getString(pos.getAndIncrement()),
                "User enabled"));
        newUserData.setAuthorities(bytesToAuthorities(set.getBytes(
                pos.getAndIncrement())));
        return newUserData;
    }

    @NonNull
    public UserData fromQuery(
            @NonNull ResultSet set,
            @NonNull AtomicInteger pos
    ) throws Exception {
        return fromQuery(set, pos, false);
    }

    @NonNull
    public UserData fromQuery(@NonNull ResultSet set)
            throws SQLException, IOException {
        return fromQuery(set, new AtomicInteger(1), false);
    }

    public void toQuery(
            @NonNull UserData user,
            @NonNull QueryProvider provider,
            AtomicInteger pos
    ) throws SQLException, IOException {
        provider.setPrepareArguments(pos.getAndIncrement(),
                user.getUsername(),
                JDBCType.NVARCHAR);
        provider.setPrepareArguments(pos.getAndIncrement(),
                user.getPassword(),
                JDBCType.NVARCHAR);
        String enabled = null;
        if(user.getEnabled() != null) {
            enabled = CastAssertions.boolToChar(user.isEnabled());
        }
        provider.setPrepareArguments(pos.getAndIncrement(), enabled,
                JDBCType.CHAR);
        provider.setPrepareArguments(pos.getAndIncrement(),
                authoritiesToBytes(user.getAuthoritiesSet()),
                JDBCType.BINARY);
    }

    @Nullable
    public UserData get(int id, boolean withPassword)
            throws Exception {
        String key = "get";
        if(!withPassword) {
            key = "get-no-pass";
        }
        getProvider().prepare(getQueryLoader().load(key));
        getProvider().setPrepareArguments(1, id,
                JDBCType.INTEGER);
        ResultSet set = getProvider().executePrepared();
        if(set.next()) {
            return fromQuery(set, new AtomicInteger(1), withPassword);
        } else {
            return null;
        }
    }

    @Nullable
    public UserData get(@NonNull String name,
                        boolean withPassword)
            throws Exception {
        String key = "get-by-name";
        if(!withPassword) {
            key = "get-by-name-no-pass";
        }
        getProvider().prepare(getQueryLoader().load(key));
        getProvider().setPrepareArguments(1, name,
                JDBCType.NVARCHAR);
        ResultSet set = getProvider().executePrepared();
        if(set.next()) {
            return fromQuery(set, new AtomicInteger(1), withPassword);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public UserData get(int id)
            throws Exception {
        return get(id, false);
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

    protected byte[] authoritiesToBytes(Set<Authority> authorities)
            throws IOException {
        ByteArrayOutputStream bytesStream =
                new ByteArrayOutputStream();
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(bytesStream)) {
            oos.writeObject(authorities);
            return bytesStream.toByteArray();
        } catch (IOException e) {
            throw new IOException("Error processing user metadata. "
                    + e.getMessage());
        }
    }
}
