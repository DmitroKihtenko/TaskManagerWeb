package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

@Repository
@Primary
@Scope(scopeName = "prototype")
public class StackConnectionsProvider extends QueryProvider {
    private int connectionSessions;

    @Autowired
    public StackConnectionsProvider(DataSource dataSource) {
        super(dataSource);
        connectionSessions = 0;
    }

    @Override
    public void connect()
            throws SQLException {
        if(connectionSessions == 0) {
            super.connect();
        }
        connectionSessions++;
    }

    @Override
    public void disconnect() {
        if(connectionSessions > 0) {
            connectionSessions--;
        }
        if(connectionSessions == 0) {
            super.disconnect();
        }
    }
}
